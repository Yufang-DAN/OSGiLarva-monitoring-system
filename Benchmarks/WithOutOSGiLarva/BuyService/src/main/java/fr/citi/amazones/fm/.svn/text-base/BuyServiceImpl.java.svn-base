package fr.citi.amazones.fm;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import static java.lang.Integer.valueOf;

public class BuyServiceImpl implements BundleActivator, BuyService {

  public void start(BundleContext bc){
    bc.registerService(BuyService.class.getName(), this, null);
  }

  public void stop(BundleContext bc){
  }

  public void hello1() {
    System.out.println("hello!*");
  }
  public void hello2() {
    System.out.println("hello!**");
  }
}
