package fr.citi.amazones.fmuse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import fr.citi.amazones.fm.BuyService;

import static java.lang.Integer.valueOf;

public class BuyClient implements BundleActivator {
  private BuyService hs;
  private static final String MAX_RUNS="fr.citi.amazones.fmuse.client.maxruns";

  private int maxruns;
  int num = 0;
   
  public void start(BundleContext context) throws Exception{
	  
    this.maxruns=valueOf(context.getProperty(MAX_RUNS));
    ServiceReference sr=context.getServiceReference(BuyService.class.getName());	
    this.hs=(BuyService)context.getService(sr);
    long start = System.currentTimeMillis();
    
    while(num<maxruns) 
    {
      this.hs.hello1();

      this.hs.hello2();
      
      num++;
      
      System.out.println("num = "+num);
    }
    
    long duration = System.currentTimeMillis()-start;
    System.out.println("duration = "+duration);
    System.err.println("maxruns = "+maxruns);
    System.err.println("duration = "+duration);
    
    System.out.println("Sortie");
    context.getBundle(0).stop();
  }

  public void stop(BundleContext context){ }
  
}

