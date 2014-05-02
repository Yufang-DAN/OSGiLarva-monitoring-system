public class BuyClient{
	
  public static void main(String args[]){
	fm.BuyService s = new fm.BuyService();
	
	int num = 0;
    long starting = System.currentTimeMillis();
	
    while(num<=100) 
    {
      s.hello1();
      s.hello1();
      
      s.hello2();
      s.hello2();
      
      num++;
      System.out.println("hello"+num);
    }
    
    long duration = System.currentTimeMillis()-starting;
    System.out.println(duration);
    
  }
}
