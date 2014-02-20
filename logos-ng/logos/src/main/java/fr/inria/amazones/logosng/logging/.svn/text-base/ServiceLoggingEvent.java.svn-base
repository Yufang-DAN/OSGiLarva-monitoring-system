package fr.inria.amazones.logosng.logging;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import java.lang.reflect.Method;

import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.EventType.CALL;
import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.EventType.RETURN;
import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.SessionEvent.STARTING;
import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.SessionEvent.CLOSING;
import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.SessionEvent.ACTIVE;
import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.SessionEvent.SESSIONLESS;
import static fr.inria.amazones.logosng.logging.ServiceLoggingEvent.SessionEvent.EXCEPTION;

import java.text.SimpleDateFormat;

import fr.inria.amazones.logosng.interception.SessionManager;

public final class ServiceLoggingEvent implements java.io.Serializable {
    transient static Map<String, BundleDescription> bundles=new HashMap<String, BundleDescription>();
    transient static Map<Method, MethodDescription> methods=new HashMap<Method, MethodDescription>();

    public enum EventType { CALL, RETURN }
    public enum SessionEvent { STARTING, CLOSING, ACTIVE, SESSIONLESS, EXCEPTION }
    
    public static final Object NO_LOG_PLACEHOLDER = "#(this value is not logged)";
    
    private final String invocationId;
    private final Date date;
    private final String bundleId;
    private final String bundleLocation;
    private final String bundleLastModified;
    private final String bundleSymbolicName;
    private final String bundleVersion;
    private final HashMap<String, Object> serviceReferences = new HashMap<String, Object>();

    private final String methodName;
    private final String methodDeclaringClasses;
    private final String methodParameterTypes;
    private final String methodReturnType;

    private final List<Object> argumentValues;
    private final Object returnValue;
    private final Set<Session> activeSessions;
    private final SessionEvent sessionEvent;
    private final EventType eventType;
    private final Session sessionEventSession;
    
    private String formattedDate="";

    private ServiceLoggingEvent(SessionEvent event, Session session, Method method, ServiceReference serviceReference, String invocationId, EventType eventType, List loggedCallValues, Object returningValue) {
        this.date = new Date();
    //Date date= new Date();
        /**/SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss:SSS");
        /**/this.formattedDate = formatter.format(date);

        Bundle bundle = serviceReference.getBundle();
        this.bundleId = Long.toString(bundle.getBundleId());
        if (!ServiceLoggingEvent.bundles.containsKey(bundleId)){
          this.bundleLocation = bundle.getLocation();
          this.bundleLastModified = Long.toString(bundle.getLastModified());
          this.bundleSymbolicName = bundle.getSymbolicName();
          this.bundleVersion = bundle.getVersion().toString();
          ServiceLoggingEvent.bundles.put(bundleId, null);
        } else {
          this.bundleLocation = null;
          this.bundleLastModified = null;
          this.bundleSymbolicName = null;
          this.bundleVersion = null;
        }
        
        this.sessionEventSession = session;
        this.sessionEvent = event;
        this.activeSessions = SessionManager.getInstance().getCurrentSessions();

        
        if (!methods.containsKey(method)){
          this.methodName = method.getName();
          this.methodParameterTypes = method.getParameterTypes().toString();
          this.methodDeclaringClasses = method.getDeclaringClass().toString();
          this.methodReturnType = method.getReturnType().toString();
          methods.put(method, new MethodDescription(this.methodName, this.methodParameterTypes, this.methodDeclaringClasses, this.methodReturnType));

        } else {
          this.methodName = methods.get(method).name;
          this.methodParameterTypes = methods.get(method).parameterTypes;
          this.methodDeclaringClasses = methods.get(method).declaringClasses;
          this.methodReturnType = methods.get(method).returnType;
        }

        for (String key : serviceReference.getPropertyKeys()) {
            this.serviceReferences.put(key, serviceReference.getProperty(key));
        }

        this.invocationId = invocationId;
        this.eventType = eventType;

        this.argumentValues = loggedCallValues;
        this.returnValue = returningValue;
    }

    //Call factories
    public static ServiceLoggingEvent callEvent(boolean sessionless, boolean newSession, Session session, Method method, ServiceReference serviceReference, String invocationId, List loggedCallValues) {
      if (sessionless){
        return new ServiceLoggingEvent(SESSIONLESS, null, method, serviceReference, invocationId, CALL, loggedCallValues, null);
      } else {
        if ( !newSession ) {
          return new ServiceLoggingEvent(ACTIVE, session, method, serviceReference, invocationId, CALL, loggedCallValues, null);
        } else {
          return new ServiceLoggingEvent(STARTING, session, method, serviceReference, invocationId, CALL, loggedCallValues, null);
        }
      }
    }

    public static ServiceLoggingEvent returnEvent(boolean sessionless, boolean newSession, Session session, Method method, ServiceReference serviceReference, String invocationId, Object returningValue) {
      if (sessionless) {
        return new ServiceLoggingEvent(SESSIONLESS, null, method, serviceReference, invocationId, RETURN, null, returningValue);
      } else {
        if ( !newSession ){
          return new ServiceLoggingEvent(ACTIVE, session, method, serviceReference, invocationId, RETURN, null, returningValue);
        } else {
          return new ServiceLoggingEvent(CLOSING, session, method, serviceReference, invocationId, RETURN, null, returningValue);
        }
      }
    }

    public static ServiceLoggingEvent exceptionEvent(Method method, ServiceReference serviceReference, List loggedCallValues, Object returningValue) {
        return new ServiceLoggingEvent(EXCEPTION, null, method, serviceReference, null, CALL, loggedCallValues, returningValue);
    }

    //Extraction
    public Map toMap() {
        HashMap hmp = new HashMap();

        Map<String, Object> data = new HashMap<String, Object>() {

            {
                put("invocation_id", invocationId);
                //put("date", date);
                put("date", formattedDate);
                put("method_name", methodName);
                put("method_declaring_class", methodDeclaringClasses);
                put("method_parameter_types", methodParameterTypes);
                put("method_return_type", methodReturnType);
                put("type", eventType);
                if (eventType == CALL) {
                    put("argument_values", argumentValues);
                } else {
                    put("return_value", returnValue);
                    put("exception_thrown", returnValue instanceof Throwable);
                }
                put("active_sessions", activeSessions);
                put("bundle_id", bundleId);
                if (bundles.containsKey(bundleId)){
                  put("bundle_location", bundles.get(bundleId).bundleLocation);
                  put("bundle_last_modified", bundles.get(bundleId).bundleLastModified);
                  put("bundle_symbolic_name", bundles.get(bundleId).bundleSymbolicName);
                  put("bundle_version", bundles.get(bundleId).bundleVersion);
                } else {
                  put("bundle_location", bundleLocation);
                  put("bundle_last_modified", bundleLastModified);
                  put("bundle_symbolic_name", bundleSymbolicName);
                  put("bundle_version", bundleVersion);
                  bundles.put(bundleId, new BundleDescription(bundleLocation, bundleLastModified, bundleSymbolicName, bundleVersion));
                }

                put("service_reference_properties", serviceReferences);
                put("session_event", sessionEvent);
                put("session", sessionEventSession);

            }
        };
        return data;
    }

    private class BundleDescription {
      private final String bundleLocation;
      private final String bundleLastModified;
      private final String bundleSymbolicName;
      private final String bundleVersion;
      private BundleDescription (String bundleLocation, String  bundleLastModified, String bundleSymbolicName, String  bundleVersion){
        this.bundleLocation = bundleLocation;
        this.bundleLastModified = bundleLastModified;
        this.bundleSymbolicName = bundleSymbolicName;
        this.bundleVersion = bundleVersion;
      }
    }

    private class MethodDescription {
      private final String name;
      private final String parameterTypes;
      private final String declaringClasses;
      private final String returnType;
      private MethodDescription (String name, String parameterTypes, String declaringClasses, String returnType){
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.declaringClasses = declaringClasses;
        this.returnType = returnType;
      }
    }



}
