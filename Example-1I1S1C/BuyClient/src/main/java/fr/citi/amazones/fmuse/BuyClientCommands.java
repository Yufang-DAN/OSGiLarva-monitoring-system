package fr.citi.amazones.fmuse;

import org.apache.felix.service.command.Descriptor;


public interface BuyClientCommands {
  public static final String SCOPE = "larva";
  public static final String [] NAMES = new String [] {"hello1", "hello2", "loadS"};

  @Descriptor("Send a hello message")
  public void hello1();
  public void hello2();
  public void loadS();
}
