package fr.citi.amazones.fm2;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import static java.lang.Integer.valueOf;

import fr.citi.amazones.fm1.BuyService;

public class BuyServiceImpl2 implements BundleActivator, BuyService {

  private ServiceRegistration registration2;
  
  public void start(BundleContext bc){
    registration2 = bc.registerService(BuyService.class.getName(), this, null);
  }

  public void stop(BundleContext bc){
	if ( registration2 != null ){
        registration2.unregister();
      }
    registration2=null;
  }

  public void hello1() {  
      System.out.println("2Servicehello!1");
    }
    
  public void hello2() {  
      System.out.println("2Servicehello!2");
    }

}
