package fr.citi.amazones.fmuse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import fr.citi.amazones.fm.BuyService;


public class BuyClient implements BundleActivator{
  private BuyService hs;

  public void start(BundleContext context) throws Exception{

    ServiceReference sr=context.getServiceReference(BuyService.class.getName());
    this.hs=(BuyService)context.getService(sr);
    
    long start=System.currentTimeMillis();
    int num = 0;
    
    while(num<10) 
    {
      this.hs.hello1();
      this.hs.hello1();
      
      this.hs.hello2();
      this.hs.hello2();
      
      num++;
      System.out.println("hello"+num);
    }
    
    long duration = System.currentTimeMillis()-start;
    System.out.println(duration);
    
  }

  public void stop(BundleContext context){ }
 
}

