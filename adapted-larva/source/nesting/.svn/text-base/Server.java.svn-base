package nesting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Server {

	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	ArrayList<Client> requests = new ArrayList<Client>();
	
	public void sendRequest()
	{
		requests.add(new Client(this));
	}
	
	public void buyIt(int id)
	{
		requests.get(requests.indexOf(new Client(id))).delete();
		requests.remove(new Client(id));
	}
	
//	public void processUser(int id)
//	{
//		users.get(users.indexOf(new User(id))).process();
//	}
	
	public static int read()
	{
		try{
		return Integer.parseInt(br.readLine());
		}
		catch(Exception ex)
		{ex.printStackTrace();}
		return -1;
	}
	
	public static void write(String text)
	{
		System.out.println(text);
	}
	
//	public void requestMenu(int id)
//	{
//		requests.get(requests.indexOf(new Client(id))).menu();
//	}
	
	public String show()
	{
		String s = "";
		for (Client a:requests)
			s += a.id + ", ";
		return s;
	}
	
	public void menu()
	{
		boolean run = true;
		while (run)
		{
			System.out.println("****MAIN MENU****");
			System.out.println("Requests: "+show());
			System.out.println("1. send request");
			System.out.println("2. buy one");
			System.out.println("3. nothing");
//			System.out.println("4. edit user");
			System.out.println("5. exit");
			switch(read())
			{
			case 1:sendRequest();break;
			case 2:write("Id: ");buyIt(read());break;
			case 3:break;
//			case 4:write("Id: ");userMenu(read());break;
			case 5:run = false;break;
			}
		}
	}
	
	public static void main(String[] args) {
		try{
		//ClassLoader.getSystemClassLoader().loadClass("larva._cls_clock0");
		
			//_larva.initialize();
		System.out.println(System.currentTimeMillis());
		new Server().menu();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
