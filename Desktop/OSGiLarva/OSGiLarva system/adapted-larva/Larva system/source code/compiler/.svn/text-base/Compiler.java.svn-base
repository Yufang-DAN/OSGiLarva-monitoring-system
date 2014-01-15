package compiler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class Compiler {

	public static String inputDir = "C:\\Users\\University User\\Desktop\\Parser\\Parser\\src\\bank 2.txt";

	public static String outputDir = ".\\";
	
	public static String textDir = "";
	
	public static String graphvizDir = "C:\\Program Files\\Graphviz2.16\\bin\\dot.exe";
	
	static Global global;
	static Methods methods;
	ParsingString ps;
	static StringBuilder imports;
	
	static boolean verbose = true;
	
	public Compiler(){}
	
	public Compiler(ParsingString sb)
	{
		ps = sb;
	}
	
	public StringBuilder getString()
	{
		return ps.string;
	}
	
	public void parse() throws ParseException
	{
		imports = parseWrapper("IMPORTS",false).string;
		
		global = new Global(parseWrapper("GLOBAL"));
		int slash = Math.max(inputDir.lastIndexOf("\\"),inputDir.lastIndexOf("/"));
		int space = inputDir.indexOf(" ",slash);
		int dot = 	inputDir.lastIndexOf(".");
		if (dot <= slash) dot = inputDir.length();//remember that there may be no slashes and no dots...
		global.name = inputDir.substring(slash+1,(space < dot && space > 0)?space:dot);
		global.parse();
		methods = new Methods(parseWrapper("METHODS", false));
//		
//		if (ps.getString().length() > 0)
//			System.out.println("Warning: Unreached end of file!!");
		//System.out.println(global);
		//System.out.println(methods.toJava());
		
	}
	
	public ParsingString parseWrapper(String title,boolean strict) throws ParseException
	{
		return ps.parseWrapper(title,strict);
	}
	
	public ParsingString parseWrapper(String title) throws ParseException
	{
		return ps.parseWrapper(title);
	}
	
	public String toJava()throws ParseException
	{
		return "";
	}
	
	public static void main(String[] args) {
		try{
			if (args.length == 0)
			{
				System.out.println("You should specify a script file!! ");
				System.out.println("-o [] to specify output directory");
				System.out.println("-g [] to specify Graphviz directory");
				System.out.println("-t [] to specify directory of the monitor text output");
			}
			else
			{
				inputDir = args[0];
				for (int i = 1; i < args.length; i++)
				{
					if (args[i].equals("-o") && i < args.length-1)
						outputDir = args[i+1]+"/";
				
					if (args[i].equals("-g") && i < args.length-1)
						graphvizDir = args[i+1];
					
					if (args[i].equals("-t") && i < args.length-1)
						textDir = args[i+1]+ "/";
				}
				
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputDir)));
				StringBuilder text = new StringBuilder();
				String temp;
				while ((temp = br.readLine()) != null)   {
					if (temp.indexOf("%%") != -1)//remove comments
						temp=temp.substring(0,temp.indexOf("%%"));
					text.append(temp.trim() + "\r\n");
				}
				Compiler p = new Compiler(new ParsingString(text));
				p.parse();
				p.outputFiles();
				p.outputLogics();
				System.out.println("Compiled Successfully!!!");
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void outputLogics() {
		global.outputLogicsDiagrams(outputDir);		
	}

	private void outputFiles()throws ParseException {
		global.toJava();		
	}

	
}
