package fr.citi.amazones.fm1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import static java.lang.Integer.valueOf;

public class BuyServiceImpl1 implements BundleActivator, BuyService {
	
  public ServiceRegistration registration1;
  
  public void start(BundleContext bc){
    registration1 = bc.registerService(BuyService.class.getName(), this, null);
  }

  public void stop(BundleContext bc){
	if ( registration1 != null ){
        registration1.unregister();
      }
    registration1=null;
  }

  public void hello1() {
      System.out.println("1Servicehello!1");
  }
  
  public void hello2() {
      System.out.println("1Servicehello!2");
  }

}
