package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logosng.storage.AppendOnlyCompressedCryptedEventStore;
import fr.inria.amazones.logosng.annotations.Hash;
import fr.inria.amazones.logosng.annotations.MethodsMatchSessions;
import fr.inria.amazones.logosng.annotations.NoLog;
import fr.inria.amazones.logosng.annotations.NoMethodLog;
import fr.inria.amazones.logosng.annotations.ReadWrite;
import fr.inria.amazones.logosng.annotations.EntryPoint;

import org.osgi.framework.*;
import org.osgi.framework.hooks.service.EventHook;
import org.osgi.framework.hooks.service.FindHook;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import java.net.InetAddress;

import static java.util.Arrays.asList;
import static java.util.logging.Level.SEVERE;
import static org.osgi.framework.ServiceEvent.*;


import larvaplug.Monitored;


public class ServiceEventAndFindHook implements EventHook, FindHook, BundleActivator {

    private static final String CONFIG_SKIPPED_BUNDLES = "logosng.aop.skip-bundles";
    private static final String CONFIG_SKIPPED_SERVICES = "logosng.aop.skip-services";
    private static final String CONFIG_TRUSTED_BUNDLES = "logosng.aop.trusted-bundles";
    private static final String CONFIG_ENFORCE_TRUSTED_BUNDLE_DATA = "logosng.aop.enforce-trusted-bundles-data";
    private static final String CXF_EXPORTED_INTERFACES = "service.exported.interfaces";
    private static final String CXF_EXPORTED_CONFIG_KEY = "service.exported.configs";
    private static final String CXF_EXPORTED_CONFIG_VALUE = "org.apache.cxf.ws";
    private static final String CXF_WS_ADDRESS = "org.apache.cxf.ws.address";
	private static final String CONFIG_NO_WEBSERVIE = "logosng.aop.nowebservice";

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Set<ServiceReference> hookedServiceReferences = new HashSet<ServiceReference>();
    private final Set<Object> proxies = new HashSet<Object>();
    private final Map<ServiceReference, Object> proxyMapping = new HashMap<ServiceReference, Object>();
    private final Map<ServiceReference, ServiceReference> proxyReferenceMapping = new HashMap<ServiceReference, ServiceReference>();
    private final Map<ServiceReference, Properties> proxyPropertyMapping = new HashMap<ServiceReference, Properties>();

    private ClassAnalyzer classAnalyzer;
    private List<String> skippedBundles;
    private Set<String> skippedServiceInterfaces;
    private boolean enforceTrustedBundleData;
    private Set<String> trustedBundles;
    private Logger internalLogger;
    private AppendOnlyCompressedCryptedEventStore serviceLogger;
    private BundleContext bc;
	private boolean nowebservice = false;


    //============================
    // Larva attributes
	//private final List<Class> RegInterfacesList = new LinkedList<Class>();//for detect service registering or re-registering at one service interface
	private final Map<Object, larvaplug._callable> interfacemonitorMapping = new HashMap<Object, larvaplug._callable>();//different monitor corresponds to different service interfaces
    

    private larvaplug._callable monitor = null;
    private Long pid;
    private String nameSI;
    //============================



    public void start(BundleContext context) throws Exception {
        this.bc = context;
        this.internalLogger = fr.inria.amazones.logging.Logger.getLogger(context, this.getClass());
        this.classAnalyzer = new ClassAnalyzer(context);
        this.serviceLogger = new AppendOnlyCompressedCryptedEventStore(context);
        this.skippedBundles = Arrays.asList(context.getProperty(CONFIG_SKIPPED_BUNDLES));
        this.skippedServiceInterfaces = new HashSet<String>();
        if (context.getProperty(CONFIG_SKIPPED_SERVICES) != null) {
          this.skippedServiceInterfaces.addAll(Arrays.asList(context.getProperty(CONFIG_SKIPPED_SERVICES).split(", *")));
        }
		if (context.getProperty(CONFIG_NO_WEBSERVIE) != null){
            this.nowebservice = "true".equals(context.getProperty(CONFIG_NO_WEBSERVIE));
		}
        this.enforceTrustedBundleData = "true".equals(context.getProperty(CONFIG_ENFORCE_TRUSTED_BUNDLE_DATA));
        this.trustedBundles = new HashSet<String>(asList(context.getProperty(CONFIG_TRUSTED_BUNDLES)));


        context.registerService(
                new String[]{
                    EventHook.class.getCanonicalName(),
                    FindHook.class.getCanonicalName()
                },
                this,
                null);
    }

    public void stop(BundleContext context) throws Exception {
    }

    public void find(BundleContext context, String name, String filter, boolean allServices, Collection references) {
		//displayStackTrace();
        //pid=new Long((long)mySecurityManager.getCallerClass(7).hashCode());
		//System.out.println("Current client id is : " + pid + " , ");
		pid=new Long(10);
        System.out.println("Current bundle name is: "+context.getBundle().getSymbolicName());

        findLarva(context, name, filter, allServices, references);

        lock.readLock().lock();
        try {
            final Iterator iterator = references.iterator();
            while (iterator.hasNext()) {
                Object reference = iterator.next();
                if (hookedServiceReferences.contains(reference)) {
                    iterator.remove();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void event(ServiceEvent event, Collection contexts) {
        if (isSkippedBundle(event.getServiceReference().getBundle())) {
            return;
        }
		
        try {
            switch (event.getType()) {
                case REGISTERED:
                    handleServiceRegistration(event);
                    break;
                    

                case UNREGISTERING:
                    handleServiceUnregistration(event);
                    break;

                case MODIFIED:
                case MODIFIED_ENDMATCH:
                    handleServicePropertiesChange(event);
                    break;
            }
        } catch (Throwable t) {
            internalLogger.log(SEVERE, "EventHook caught an exception", t);
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    private boolean isSkippedBundle(Bundle bundle) {
        return this.skippedBundles.contains(bundle.getSymbolicName());
    }

    private void handleServiceUnregistration(ServiceEvent event){
        final ServiceReference serviceReference = event.getServiceReference();
  
        if (isNotHooked(serviceReference)) {
            return;
        }

        lock.writeLock().lock();
        try {
            final Object proxy = proxyMapping.get(serviceReference);
		      
            hookedServiceReferences.remove(serviceReference);
            proxies.remove(proxy);
            proxyMapping.remove(serviceReference);
            proxyReferenceMapping.remove(serviceReference);
            proxyPropertyMapping.remove(serviceReference);

            handleServiceUnregistrationLarva(event);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isNotHooked(ServiceReference serviceReference) {
        return !hookedServiceReferences.contains(serviceReference);
    }

    private void handleServicePropertiesChange(ServiceEvent event) {
        final ServiceReference serviceReference = event.getServiceReference();
        if (isNotHooked(serviceReference)) {
            return;
        }

        lock.writeLock().lock();
        try {
            Properties properties = proxyPropertyMapping.get(serviceReference);
            properties.clear();
            for (String key : serviceReference.getPropertyKeys()) {
                properties.put(key, serviceReference.getProperty(key));
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void handleServiceRegistration(ServiceEvent event) throws Exception {
		handleServiceRegistrationLarva(event);
        final ServiceReference serviceReference = event.getServiceReference();
        if (hookedServiceReferences.contains(serviceReference)) {
            return;
        }

        final Object service = serviceReference.getBundle().getBundleContext().getService(serviceReference);
        System.out.println("service is : " + service);
		
        if (service.getClass().equals(ServiceEventAndFindHook.class)) {
            return;
        } else if (proxies.contains(service)) {
            return;
        }

        final String[] implementedInterfaces = (String[]) event.getServiceReference().getProperty("objectClass");        

        for (String interfaceName : implementedInterfaces) {
            if (this.skippedServiceInterfaces.contains(interfaceName)) {
                return;
            }
        }
        
        final Class[] implementedInterfacesClasses = new Class[implementedInterfaces.length];
		final List<String> exportedInterfacesList = new LinkedList<String>();
        for (int i = 0; i < implementedInterfaces.length; i++) {
            implementedInterfacesClasses[i] = serviceReference.getBundle().loadClass(implementedInterfaces[i]);
            List<Method> classMethods = asList(implementedInterfacesClasses[i].getMethods());
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(EntryPoint.class)){
                    exportedInterfacesList.add(implementedInterfaces[i]);
                    break;                
                }
            }
        }
	        
        String[] propertyKeys = event.getServiceReference().getPropertyKeys();
        Properties properties = new Properties();
        for (String key : propertyKeys) {
            properties.put(key, event.getServiceReference().getProperty(key));
        }

        ClassLoader proxyClassLoader = new PiggyBackClassLoader(
                asList(
                service.getClass().getClassLoader(),
                ServiceEventAndFindHook.class.getClassLoader()));

        InvocationHandler invocationHandler = getSuitableInvocationHandler(serviceReference, service, implementedInterfacesClasses, proxyClassLoader);
        Object proxy = Proxy.newProxyInstance(
                proxyClassLoader,
                implementedInterfacesClasses,
                invocationHandler);

        lock.writeLock().lock();
        try {
            hookedServiceReferences.add(serviceReference);
            proxies.add(proxy);
            proxyMapping.put(serviceReference, proxy);
            if (!exportedInterfacesList.isEmpty() && nowebservice==false){
				final String[] exportedInterfaces = new String[exportedInterfacesList.size()];
				for (int i = 0; i < exportedInterfacesList.size(); i++){
					exportedInterfaces[i] = exportedInterfacesList.get(i);
				}
				properties.put(CXF_EXPORTED_INTERFACES, "*");//should be <code>exportedInterfaces</code> instead of <code>"*"</code>, bug of cxf-dosgi
				properties.put(CXF_EXPORTED_CONFIG_KEY, CXF_EXPORTED_CONFIG_VALUE);
				InetAddress addr = InetAddress.getLocalHost();
				properties.put(CXF_WS_ADDRESS, "http://"+addr.getHostName()+":9000/"+exportedInterfaces[0].replace('.', '/'));	
			}
			
            ServiceReference proxyReference = serviceReference.getBundle().getBundleContext().registerService(implementedInterfaces, proxy, properties).getReference();
            proxyReferenceMapping.put(serviceReference, proxyReference);
            proxyPropertyMapping.put(serviceReference, properties);

            
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected InvocationHandler getSuitableInvocationHandler(ServiceReference serviceReference, Object service, Class[] implementedInterfacesClasses, ClassLoader proxyClassLoader) {
        InvocationHandler invocationHandler;
        if (classAnalyzer.haveOnlySerializableData(implementedInterfacesClasses)) {
            invocationHandler = createProxy(
                    proxyClassLoader,
                    service,
                    serviceReference,
                    implementedInterfacesClasses);
        } else {
            internalLogger.log(
                    SEVERE,
                    String.format(
                    "One of those service interfaces manipulates non-serializable types: %s",
                    Arrays.toString(implementedInterfacesClasses)));

            invocationHandler = new SecurityEnforcementProxy(
                    service,
                    serviceReference,
                    serviceLogger);
        }
        return invocationHandler;
    }

    public InvocationHandler createProxy(ClassLoader proxyClassLoader, Object service, ServiceReference serviceReference, Class[] implementedInterfacesClasses) {
        final Set<Method> sessionMethods = new HashSet<Method>();
        final List<Method> methods = new LinkedList<Method>();
        for (Class interfaceClass : implementedInterfacesClasses) {
            final List<Method> interfaceMethods = asList(interfaceClass.getMethods());
            methods.addAll(interfaceMethods);
            /*if (interfaceClass.isAnnotationPresent(MethodsMatchSessions.class)) {
                sessionMethods.addAll(interfaceMethods);
            }*/
        }

        final Set<Method> unLoggedMethods = new HashSet<Method>();
        final Map<Method, List<ValueHandler>> callValueHandlers = new HashMap<Method, List<ValueHandler>>();
        final Map<Method, ValueHandler> returnValueHandlers = new HashMap<Method, ValueHandler>();

        final ValueHandler hashValueHandler = new HashValueHandler();
        final ValueHandler readWriteValueHandler = new ReadWriteValueHandler();
        final ValueHandler noLogValueHandler = new NoLogValueHandler();
        final ValueHandler normalValueHandler = new NormalValueHandler();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(NoMethodLog.class)){
                if (method.isAnnotationPresent(EntryPoint.class)){
                  sessionMethods.add(method);
                }

                ValueHandler returnValueHandler = normalValueHandler;
                if (method.isAnnotationPresent(Hash.class)) {
                    returnValueHandler = hashValueHandler;
                } else if (method.isAnnotationPresent(NoLog.class)) {
                    returnValueHandler = noLogValueHandler;
                } else if (method.isAnnotationPresent(ReadWrite.class)) {
                    returnValueHandler = readWriteValueHandler;
                } 
                returnValueHandlers.put(method, returnValueHandler);
    
                final List<ValueHandler> parameterValueHandlers = new LinkedList<ValueHandler>();
                for (Annotation[] annotations : method.getParameterAnnotations()) {
                    ValueHandler parameterValueHandler = normalValueHandler;
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().equals(Hash.class)) {
                            parameterValueHandler = hashValueHandler;
                            break;
                        } else if (annotation.annotationType().equals(NoLog.class)) {
                            parameterValueHandler = noLogValueHandler;
                            break;
                        } else if (annotation.annotationType().equals(ReadWrite.class)) {
                            parameterValueHandler = readWriteValueHandler;
                            break;
                        }
                    }
                    parameterValueHandlers.add(parameterValueHandler);
                }
                callValueHandlers.put(method, parameterValueHandlers);
            } else {
                unLoggedMethods.add(method);
            }

        }
        return new LoggingProxy(
                this.bc,
                proxyClassLoader,
                service,
                serviceReference,
                unLoggedMethods,
                callValueHandlers,
                returnValueHandlers,
                sessionMethods,
                enforceTrustedBundleData,
                trustedBundles,
                serviceLogger,
                monitor);
    }



    public void findLarva(BundleContext context, String name, String filter, boolean allServices, Collection references) {

        final String larvaClassName = "larva._monitor0";
        nameSI=name;

		try{
            //********************
            // DEBUG trace 
			//System.out.println("name= "+name); // Ex : fr.citi.buyservice
			String s1="[";
			String s2="]";
            String interfaceName = s1+name+s2;
            // END DEBUG trace 
            //********************
            
            //find the current service interface name
            final String serviceInterfaceName1;
		    int num1 = name.lastIndexOf('.');
		    if(num1!=-1){
			   serviceInterfaceName1 = name.substring(num1+1);
			   }else serviceInterfaceName1 = null; 
		     //find the current service interface name

            //larvaplug._callable monitor = null;
			if (interfacemonitorMapping.containsKey(name) && hookedServiceReferences.toString().contains(interfaceName)) {
                monitor = interfacemonitorMapping.get(name);
                monitor._call("LoadS", pid, nameSI); // TODO: add the correct clientName.


            }else if (interfacemonitorMapping.containsKey(name) && !hookedServiceReferences.toString().contains(interfaceName)){
                monitor = interfacemonitorMapping.get(name);
                monitor._call("nogetS", pid, nameSI);  // TODO: add the correct clientName.


		    } else if(!interfacemonitorMapping.containsKey(name)&& !hookedServiceReferences.toString().contains(interfaceName)){
                // If no service currently in memory, then nothing to monitor.
                
            } 
        }catch (Exception e){
            internalLogger.log(SEVERE, "Larva Error, Monitor Class not found. No corresponding larva monitor: "+larvaClassName, e);
        }
    }

    private void handleServiceUnregistrationLarva(ServiceEvent event) {
        try {
			final ServiceReference serviceReference1 = event.getServiceReference();
			final String[] implementedInterfaces1 = (String[]) event.getServiceReference().getProperty("objectClass");     
            final Class[] implementedInterfacesClasses1 = new Class[implementedInterfaces1.length];

            //find the current service interface name
            final String serviceInterfaceName2;
		    int num2 = implementedInterfaces1[0].lastIndexOf('.');
		    if(num2!=-1){
			   serviceInterfaceName2 = implementedInterfaces1[0].substring(num2+1);
			   }else serviceInterfaceName2 = null; 
		     //find the current service interface name

            for (int ii = 0; ii < implementedInterfaces1.length; ii++) {
			   implementedInterfacesClasses1[ii] = serviceReference1.getBundle().loadClass(implementedInterfaces1[ii]);
			   if(implementedInterfacesClasses1[ii].isAnnotationPresent(Monitored.class) && interfacemonitorMapping.containsKey(implementedInterfaces1[ii])){
				  String larvaClassName = "larva._monitor0";	
				  try{
					  monitor = interfacemonitorMapping.get(implementedInterfaces1[ii]);
					  monitor._call("UnLoaded", pid, implementedInterfaces1[ii]);
					  } catch (Exception e){
                            internalLogger.log(SEVERE, "Larva Error, Monitor Class not found.\n\t"+ implementedInterfacesClasses1 +": has no corresponding larva monitor: "+larvaClassName, e);
                            }
                }
            }

        } catch (Throwable t) {
            internalLogger.log(SEVERE, "EventHook caught an exception", t);
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }


    private void handleServiceRegistrationLarva(ServiceEvent event) throws Exception {
        larvaplug._callable monitor1 = null;

        final ServiceReference serviceReference1 = event.getServiceReference();
        
        final String[] implementedInterfaces1 = (String[]) event.getServiceReference().getProperty("objectClass");
              
        final Class[] implementedInterfacesClasses1 = new Class[implementedInterfaces1.length];

        /*for (int ii = 0; ii < implementedInterfaces1.length; ii++) {
            implementedInterfacesClasses1[ii] = serviceReference1.getBundle().loadClass(implementedInterfaces1[ii]);
            
            if(implementedInterfacesClasses1[ii].isAnnotationPresent(Monitored.class)){
                String larvaClassName1 = "larva._monitor";
                try{ 
                    Class c1 = Class.forName(larvaClassName1);
                    if (this.monitor == null) {
                        this.monitor = (larvaplug._callable)c1.newInstance(); 
                    }
                    if(RegInterfacesList.contains(implementedInterfacesClasses1[ii])){
                        this.monitor._call("ReregS");
                    }
                    else {
                        this.monitor._call("RegS");
                        RegInterfacesList.add(implementedInterfacesClasses1[ii]);
                    }
                } catch (Exception e){
                         internalLogger.log(SEVERE, "Larva Error, Monitor Class not found.\n\t"+ (implementedInterfacesClasses1[ii]) +": has no corresponding larva monitor: "+larvaClassName1, e);
                }
            }
        }*/							 
		     
		for (int ii = 0; ii < implementedInterfaces1.length; ii++) {
			implementedInterfacesClasses1[ii] = serviceReference1.getBundle().loadClass(implementedInterfaces1[ii]);
			if(implementedInterfacesClasses1[ii].isAnnotationPresent(Monitored.class) && !interfacemonitorMapping.containsKey(implementedInterfaces1[ii])){
				String larvaClassName1 = "larva._monitor0";	
				try{ 
					Class c1 = Class.forName(larvaClassName1);
					monitor1 = (larvaplug._callable)c1.newInstance();
                    interfacemonitorMapping.put(implementedInterfaces1[ii], monitor1);
                    monitor=monitor1;
					}catch (Exception e){
                           internalLogger.log(SEVERE, "Larva Error, Monitor Class not found.\n\t"+ (implementedInterfacesClasses1[ii]) +": has no corresponding larva monitor: "+larvaClassName1, e);
                            }
					}
			} 
    }
    
        // Trying to find caller
/*
    static class MySecurityManager extends SecurityManager {
          public Class getCallerClass(int callStackDepth) {
              return getClassContext()[callStackDepth];
          }
    }

    private final static MySecurityManager mySecurityManager = new MySecurityManager();


    private void displayStackTrace() {
        try{
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("@ Call stack");
            int i=0;
            while(true) {
                Class c = mySecurityManager.getCallerClass(i);
                System.out.println("@   + "+c.getName() + " (hash : "+c.hashCode()+")");
                i++;
            }
        } catch(Exception e) {
        }
    }

}*/
