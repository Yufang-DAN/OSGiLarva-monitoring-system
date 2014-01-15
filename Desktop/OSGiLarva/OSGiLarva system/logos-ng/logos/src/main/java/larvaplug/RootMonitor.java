package larvaplug;

import java.util.LinkedHashMap;

public abstract class RootMonitor implements _callable{

  static protected RootMonitor root;
  static protected LinkedHashMap<RootMonitor,RootMonitor> instances = new LinkedHashMap<RootMonitor,RootMonitor>();

  public static void activateMonitor(String interfaceNameMethodName, Long pid, String serviceInterface) {
  //interfaceNameMethodName is meaning interfaceName.methodName
  //serviceInterface is meaning [package.interfaceName]
  //_get_cls_inst()._call(methodName,larva.StatesDefsBuyService0.getInstance().getEventNumber(methodName));
  _get_cls_inst()._call(interfaceNameMethodName, pid, serviceInterface);
  _get_cls_inst()._call_all_filtered(interfaceNameMethodName, pid, serviceInterface);
  }

  public static RootMonitor _get_cls_inst() {
    synchronized(instances) {
      return root;
    }
  }
}
