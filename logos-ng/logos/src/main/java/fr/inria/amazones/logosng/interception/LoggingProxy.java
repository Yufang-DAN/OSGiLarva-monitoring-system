
package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logosng.storage.AppendOnlyCompressedCryptedEventStore;
import fr.inria.amazones.logosng.logging.LogosSecurityException;
import fr.inria.amazones.logosng.logging.ServiceLoggingEvent;
import fr.inria.amazones.logosng.logging.Session;
import fr.inria.amazones.logosng.annotations.EntryPoint;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.lang.annotation.Annotation;
import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;

import java.text.SimpleDateFormat;

import larvaplug.Monitored;

public class LoggingProxy implements InvocationHandler {

    private static final String CONFIG_SESSIONLESS = "logosng.protected.sessionless";
    
    private final ClassLoader proxyClassLoader;
    private final Object service;
    private final ServiceReference serviceReference;
    private final Set<Method> unLoggedMethods;
    private final Map<Method, List<ValueHandler>> callValueHandlers;
    private final Map<Method, ValueHandler> returnValueHandlers;
    private final Set<Method> sessionMethods;
    private final Logger internalLogger;
    private final AppendOnlyCompressedCryptedEventStore serviceLogger;
    private final boolean enforceTrustedBundleData;
    private final boolean sessionless;
    private final Set<String> trustedBundles;

    private ClassAnalyzer classAnalyzer;


    private larvaplug._callable monitor = null;
    private Long pid;

    public LoggingProxy(BundleContext bc,
                        ClassLoader proxyClassLoader,
                        Object service,
                        ServiceReference serviceReference,
                        Set<Method> unLoggedMethods,
                        Map<Method, List<ValueHandler>> callValueHandlers,
                        Map<Method, ValueHandler> returnValueHandlers,
                        Set<Method> sessionMethods,
                        boolean enforceTrustedBundleData,
                        Set<String> trustedBundles,
                        AppendOnlyCompressedCryptedEventStore serviceLogger,
                        larvaplug._callable monitor) {
        internalLogger = fr.inria.amazones.logging.Logger.getLogger(bc, this.getClass());
        this.classAnalyzer = new ClassAnalyzer(bc);
        this.proxyClassLoader = proxyClassLoader;
        this.service = service;
        this.serviceReference = serviceReference;
        this.unLoggedMethods = unLoggedMethods;
        this.callValueHandlers = callValueHandlers;
        this.returnValueHandlers = returnValueHandlers;
        this.sessionMethods = sessionMethods;
        this.enforceTrustedBundleData = enforceTrustedBundleData;
        this.sessionless = "true".equals(bc.getProperty(CONFIG_SESSIONLESS));
        this.trustedBundles = trustedBundles;
        this.serviceLogger = serviceLogger;
        this.monitor=monitor;
    }

    private boolean shallLogReturnValue(Method method) {
        return returnValueHandlers.containsKey(method);
    }

    private boolean shallLogArgumentValues(Method method) {
        return callValueHandlers.containsKey(method);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//displayStackTrace();
		pid=new Long((long)mySecurityManager.getCallerClass(3).hashCode());
		//System.out.println("Current client id is : " + pid + " , ");

        Object retVal = null;
        Object retValLogged = null;
        
        if (!unLoggedMethods.contains(method)){
        /**/Date date= new Date();
        /**/SimpleDateFormat formatter = new SimpleDateFormat("SSS");
        /**/String formattedDate = formatter.format(date);
            long threadId = currentThread().getId();
            final String invocationId = format("%d@%d", /*Long.parseLong(formattedDate)*/new Date().getTime(), threadId);

            internalLogger.log(FINE, "Invocation Id :"+invocationId);

            List<Object> loggedCallValues;
            Session session = null;
            Object[] argsCopy = args;
            boolean newSession = false;

            try {
                if (enforceTrustedBundleData && args != null) {
                    for (Object arg : args) {
                        final Bundle[] installedBundles = serviceReference.getBundle().getBundleContext().getBundles();
                        if (!classAnalyzer.originatesFromTrustedBundle(arg, trustedBundles, installedBundles)) {
                            throw new LogosSecurityException(
                                format("An instance of class %s does not originate from a trusted classloader.",
                                       arg.getClass().getCanonicalName()));
                        }
                    }
                }

                if (!sessionless && shallLogArgumentValues(method)){
                    if (sessionMethods.contains(method)){
                        SessionManager.getInstance().registerSession(threadId);
                        newSession = true;
                    } else if (SessionManager.getInstance().getCurrentSessions().isEmpty()) {
                       throw new LogosSecurityException(format("SessionLess method %s is called without active session.", method.getName()));
                    } 
                }

                session = SessionManager.getInstance().getSession(threadId);
                
                if (shallLogArgumentValues(method)){
                    argsCopy = copyArgs(method, args);
    
                    loggedCallValues = interceptCall(method, argsCopy);
                    this.serviceLogger.log(
                            ServiceLoggingEvent.callEvent(
                                    sessionless,
                                    newSession,
                                    session,
                                    method,
                                    serviceReference,
                                    invocationId,
                                    loggedCallValues));
                }

                retVal = performInvocation(method, argsCopy);

            } catch (LogosSecurityException securityException) {
                internalLogger.log(SEVERE, "Possible security breach attempt.\n\t"+securityException.getMessage(), securityException);
                if (shallLogArgumentValues(method)){
                    argsCopy = copyArgs(method, args);
    
                    loggedCallValues = interceptCall(method, argsCopy);
                    this.serviceLogger.log(
                            ServiceLoggingEvent.callEvent(
                                    sessionless,
                                    newSession,
                                    session,
                                    method,
                                    serviceReference,
                                    invocationId,
                                    loggedCallValues));
                }
                retVal = securityException;
            }
    
            if (shallLogReturnValue(method)) {
              retVal = interceptReturn(method, retVal);

              this.serviceLogger.log(
                        ServiceLoggingEvent.returnEvent(
                                sessionless,
                                newSession,
                                session,
                                method,
                                serviceReference,
                                invocationId,
                                returnValueHandlers.get(method).handleValue(retVal)));
            }
                                

            if (!sessionless && newSession) {
               SessionManager.getInstance().unregisterSession(threadId);
            }

        } else {
            retVal = performInvocation(method, args);
        }

        Class mon = method.getDeclaringClass();
        
        //find the current service interface name
        final String serviceInterfaceName;
		int num = mon.getName().lastIndexOf('.');
		if(num!=-1){
			serviceInterfaceName = mon.getName().substring(num+1);
			}else serviceInterfaceName = null; 
		//find the current service interface name
        
        String point = ".";
        String interfacePlusMethod = serviceInterfaceName+point+method.getName();
        //System.out.println("interfacePlusMethod= "+ interfacePlusMethod);
        
        if (mon.isAnnotationPresent(Monitored.class)){
			
            String larvaClassName = "larva._monitor0";
            try {
                if (this.monitor != null) {
                    this.monitor._call(interfacePlusMethod, pid, mon.getName());
                }
             
            } catch (Exception e){
                internalLogger.log(SEVERE, "Larva Error, Monitor Class not found.\n\t"+mon+": has no corresponding larva monitor: "+larvaClassName, e);
            }
        } 
        return performReturn(retVal);
    }

    private Object[] copyArgs(Method method, Object[] args) throws IOException, ClassNotFoundException {
        if (args == null) {
            return null;
        }
        final Object[] handlers = callValueHandlers.get(method).toArray();
        Object[] copy = new Object[args.length];

        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i].getClass().equals(ReadWriteValueHandler.class) ||
                    handlers[i].getClass().equals(NoLogValueHandler.class)) {
                copy[i] = args[i];
            } else {
                copy[i] = cloneThroughSerialization((Serializable) args[i]);
            }
        }

        return copy;
    }

    private Object cloneThroughSerialization(Serializable object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        out.writeObject(object);
        out.close();

        final byte[] serializedObject = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
        ObjectInputStream in = new ObjectInputStream(byteArrayInputStream) {

            @Override
            public Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                try {
                    return proxyClassLoader.loadClass(desc.getName());
                } catch (ClassNotFoundException e) {
                    return super.resolveClass(desc);
                }
            }
        };
        Object cloned = in.readObject();
        in.close();

        return cloned;
    }

    private List<Object> interceptCall(Method method, Object[] args) {
        final List<Object> loggedCallValues = new LinkedList<Object>();

        int i = 0;
        for (ValueHandler handler : callValueHandlers.get(method)) {
            loggedCallValues.add(handler.handleValue(args[i]));
            i = i + 1;
        }

        return loggedCallValues;
    }

    private Object interceptReturn(Method method, Object retVal) throws ClassNotFoundException, IOException {
        final ValueHandler valueHandler = returnValueHandlers.get(method);
        if (valueHandler.getClass().equals(NormalValueHandler.class)) {
            return cloneThroughSerialization((Serializable) retVal);
        }
        return retVal;
    }

    private Object performReturn(Object retVal) throws Throwable {
        if (retVal instanceof Throwable) {
            throw (Throwable) retVal;
        } else {
            return retVal;
        }
    }

    private Object performInvocation(Method method, Object[] args) throws ClassNotFoundException, IOException {
        Object retVal;
        try {
            retVal = method.invoke(service, args);
        } catch (Throwable t) {
            retVal = t;
        }

        ValueHandler valueHandler = returnValueHandlers.get(method);
        if (valueHandler == null
                || valueHandler.getClass().equals(ReadWriteValueHandler.class)
                || valueHandler.getClass().equals(NoLogValueHandler.class)) {
            return retVal;
        }

        return cloneThroughSerialization((Serializable) retVal);
    }







    // Trying to find caller

    static class MySecurityManager extends SecurityManager {
          public Class getCallerClass(int callStackDepth) {
              return getClassContext()[callStackDepth];
          }
    }

    private final static MySecurityManager mySecurityManager = new MySecurityManager();


    /*private void displayStackTrace() {
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
    }*/

}
