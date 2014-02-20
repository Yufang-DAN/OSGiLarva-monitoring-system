package fr.citi.amazones.fmuse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import fr.citi.amazones.fm1.BuyService;


import java.util.Dictionary;;
import java.util.Hashtable;;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.CommandProcessor;;

import static java.lang.Integer.valueOf;

public class BuyClient implements BundleActivator, BuyClientCommands {
  private BuyService bs;
  BundleContext context;
  ServiceRegistration sr1;

  public void start(BundleContext context) throws Exception{  
	Dictionary<String, Object> shell = new Hashtable<String, Object>();
    shell.put(CommandProcessor.COMMAND_SCOPE, BuyClientCommands.SCOPE);
    shell.put(CommandProcessor.COMMAND_FUNCTION, BuyClientCommands.NAMES);
    sr1=context.registerService(BuyClientCommands.class.getName(), this, shell);
    this.context=context;

}

  public void stop(BundleContext context){
	  System.out.println("Goodbye World!!");  
	  
	  if ( sr1 != null ){
          sr1.unregister();
        }
      sr1=null; 
	  
	   }
  
  public void loadS(){
	  ServiceReference srf=this.context.getServiceReference(BuyService.class.getName());
      this.bs=(BuyService)this.context.getService(srf);
	  }

  public void hello1(){
    this.bs.hello1();
  }
  
  public void hello2(){
    this.bs.hello2();
  }
}

