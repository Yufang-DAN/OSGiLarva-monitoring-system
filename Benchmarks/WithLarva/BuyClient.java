public class BuyClient{
	
  public static void main(String args[]){
    BuyService s = new BuyService();
    int num = 0;
    long start=System.currentTimeMillis();

    while(num<100000) 
    {
	  
      s.hello1();

      s.hello2();
      
      num++;
      System.out.println("hello"+num);
    }
    long duration = System.currentTimeMillis()-start;
    System.out.println("duration = "+duration);
    System.err.println("num = "+num);
    System.err.println("duration = "+duration);
  }
}
