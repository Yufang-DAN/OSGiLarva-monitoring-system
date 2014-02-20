package fr.citi.amazones.fmuse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import fr.citi.amazones.fm1.BuyService;

/*import java.util.Dictionary;;
import java.util.Hashtable;;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.CommandProcessor;;*/

import static java.lang.Integer.valueOf;

public class BuyClient implements BundleActivator{//, BuyClientCommands {
  private BuyService bs;
  private BuyService bs1;

  public void start(BundleContext context) throws Exception{  

		ServiceReference sr=context.getServiceReference(BuyService.class.getName());
		this.bs=(BuyService)context.getService(sr);
		System.out.println("Client gets S1!");
		
		this.bs.hello1();
		System.out.println("Client invoks m1 from S1!");
		
		context.getBundle(9).stop();
		System.out.println("S1 Unregistered!");
		
		context.getBundle(10).start();
		System.out.println("S2 Registered!");
		
		ServiceReference sr1=context.getServiceReference(BuyService.class.getName());
		this.bs1=(BuyService)context.getService(sr1);
		System.out.println("Client gets S2!");
		
		this.bs1.hello2();
		System.out.println("Client invoks m2 from S2!");
		
		context.getBundle(10).stop();
		System.out.println("S2 Unregistered!");
		
		context.getBundle(9).start();
		System.out.println("S1 Registered!");

}

  public void stop(BundleContext context){
	   }
  
  /*public void loadS(){
	  ServiceReference sr=this.context.getServiceReference(BuyService.class.getName());
      this.bs=(BuyService)this.context.getService(sr);
	  }

  public void requestS1M(){
    this.bs.hello1();
  }
  
  public void requestS2M(){
    this.bs.hello2();
  }*/
}

