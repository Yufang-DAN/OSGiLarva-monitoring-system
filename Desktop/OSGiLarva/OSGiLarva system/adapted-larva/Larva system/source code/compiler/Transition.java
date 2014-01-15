package compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Transition {

	State source;
	ArrayList<Arrow> arrows = new ArrayList<Arrow>();
	
	public int addArrow(int cnt, ArrayList<Token> tokens, HashMap<String, State> all, HashMap<String, Trigger> events, Global g) throws ParseException
	{		
		if (tokens.get(cnt++).isNot("->"))
			throw new ParseException("Missing Delimiter \"->\"");
		
		Arrow a = new Arrow();
		cnt = a.parse(cnt,tokens,all, events,g);
		arrows.add(a);
		return cnt;
	}

	public int parse(int cnt, ArrayList<Token> tokens, HashMap<String, State> all, HashMap<String, Trigger> events, Global g) throws ParseException 
	{
		if (all.containsKey(tokens.get(cnt).text))
		{
			source = all.get(tokens.get(cnt).text);
			cnt++;
		}
		else
			throw new ParseException("Unknown State : " + tokens.get(cnt).text);
		
		cnt = addArrow(cnt, tokens, all, events,g);
		return cnt;
	}
	
	public void appendJava(StringBuilder sb, Property l, Global g)throws ParseException 
	{	
		sb.append("\r\n		if (1==0){}");
		for (Arrow a:arrows)
		{
			///////////Date: 5/12/11
			//////////Code removed here for the France version
			//////////Note: now code compares strings
			//////////
			String triggername = a.trigger.name.toString();
			if (a.trigger instanceof EventCollection)
			{
				EventCollection ec = ((EventCollection) a.trigger);
				if (ec.events.size()==1 && ec.events.get(0) instanceof Event)
				{
				Event e = ((Event)ec.events.get(0));
				if (e.type.equals(Event.EventType.clock))
					triggername = e.clockName.text+ "AT" + (e.clockAmount);
				else if (e.type.equals(Event.EventType.channel))
					triggername = "receiveON" + e.channelName.text;
				}
			}
			
			//sb.append("\r\n		else if (_infoSM.equals(\""+triggername+"\")");//DYF/05/12/2013
			sb.append("\r\n		else if (_infoSISM.equals(\""+triggername+"\")");
			
			
			if (a.condition.size() > 0)
				sb.append(" && ("+Global.handleVariableReplacement(a.condition,g)+")");
			sb.append("){");
			sb.append("\r\n		"+Global.redirectOutput(Global.handleVariableReplacement(a.action,g)));
			sb.append("\r\n		_state_id_"+l.name+" = "+a.destination.id+";//moving to state "+a.destination);
			if (a.destination.code != null)
				sb.append("\r\n" +Global.redirectOutput(Global.handleVariableReplacement(a.destination.code,g)));

			for (String s : a.checks.keySet())
			{
				boolean enable = a.checks.get(s);
				sb.append("\r\n" + s + "_enb = " +enable +";");
				if (enable)
				{
					Invariant inv = g.invariants.invariants.get(s);
					sb.append(inv.name + "_temp = "+ Tokenizer.showStats(inv.call) + ";");				
				}
			}

				sb.append("\r\n		_goto_"+l.name+"(_infoSISM);");
				//sb.append("\r\n		_goto_"+l.name+"(_infoSM);");//DYF/05/12/2013
			sb.append("\r\n		}");
		}
	} 
	
}
