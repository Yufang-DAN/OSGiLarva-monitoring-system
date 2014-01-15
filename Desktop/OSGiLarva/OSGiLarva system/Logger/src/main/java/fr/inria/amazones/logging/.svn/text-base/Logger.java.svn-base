package fr.inria.amazones.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogManager;
import java.util.logging.Level;
import org.osgi.framework.BundleContext;

public class Logger extends java.util.logging.Logger{
  private static LogManager lm=LogManager.getLogManager();

  protected Logger(String name, String resourceBundleName){
    super(name, resourceBundleName);
  }

  public static java.util.logging.Logger getLogger(String name){
    System.out.println("ERROR : fr.inria.amazones.logging.Logger.getLogger(String name) is disabled.");
    System.out.println("ERROR : Please use fr.inria.amazones.logging.Logger.getLogger(Class name)");
    System.exit(0);
    return null;
  }

  public static java.util.logging.Logger getLogger(Class name){
    return getLogger(null, name);
  }

  public static java.util.logging.Logger getLogger(BundleContext bc, Class name){
    java.util.logging.Logger l=lm.getLogger(name.getName());
    if (l==null){
      l=java.util.logging.Logger.getLogger(name.getName());
      l.setUseParentHandlers(false);
      String lvl;
      if (bc != null){
        lvl=bc.getProperty(name.getName()+".level");
        if (lvl==null){
          lvl=bc.getProperty(name.getPackage().getName()+".level");
          if (lvl==null){
            lvl="INFO";
          }
        } 
      }else{
        lvl="INFO";
      }
      Level level=java.util.logging.Level.parse(lvl);
      ConsoleHandler ch=new ConsoleHandler();
      ch.setFormatter(new LogFormatter());
      ch.setLevel(level);
      l.addHandler(ch);
      l.setLevel(level);
    }
    return l;
  }
}
