package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logosng.annotations.NoLog;
import fr.inria.amazones.logosng.annotations.NoMethodLog;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import static java.lang.Boolean.TRUE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.FINE;

public class ClassAnalyzer {

    private final WeakHashMap<Class, Boolean> trustedClassesCache = new WeakHashMap<Class, Boolean>();
    private Logger internalLogger;

    public ClassAnalyzer(BundleContext bc){
      this.internalLogger = fr.inria.amazones.logging.Logger.getLogger(bc, this.getClass());
    }

    public boolean haveOnlySerializableData(Class[] interfaces) {
        final List<Class> manipulatedTypes = new LinkedList<Class>();
        for (Class klass : interfaces) {
            for (Method method : klass.getMethods()) {
                if (!method.isAnnotationPresent(NoMethodLog.class)) {
                    Class[] parameterClasses = method.getParameterTypes();
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                    for (int i = 0; i < parameterClasses.length; i++) {
                        boolean shouldAdd = true;
                        for (Annotation annotation : parameterAnnotations[i]) {
                            if (annotation.annotationType().equals(NoLog.class)) {
                                shouldAdd = false;
                                break;
                            }
                        }
                        if (shouldAdd) {
                            manipulatedTypes.add(parameterClasses[i]);
                            this.internalLogger.log(FINE, "<"+method+ "> Logged parameter: " + parameterClasses[i]);
                        } else {
                            this.internalLogger.log(INFO, "<"+method+"> Parameter " + parameterClasses[i] + " not logged");
                        }
                    }
                    if (method.getAnnotation(NoLog.class) == null) {
                        manipulatedTypes.add(method.getReturnType());
                        this.internalLogger.log(FINE, "<"+method+ "> Return type " + method.getReturnType()  + " logged");
                    } else {
                        this.internalLogger.log(INFO, "<"+method+ "> Return type " + method.getReturnType() + " not logged");
                    }
                } else {
                    this.internalLogger.log(INFO, "Method : " + method + " has @NoLogMethod annotation");
                    //this.internalLogger.log(INFO, "manipulatedTypes= " + manipulatedTypes + ",");
                }
            }
        }
        return areAllSerializableClasses(manipulatedTypes);
    }

    public boolean areAllSerializableClasses(List<Class> classes) {
        for (Class klazz : classes) {
            if (!canBeSerialized(klazz)) {
                this.internalLogger.log(INFO, "non serialisable : " + klazz);
                return false;
            }
        }
        return true;
    }

    public boolean canBeSerialized(Class klazz) {
        return klazz.isPrimitive()
                || Serializable.class.isAssignableFrom(klazz)
                || Void.class.equals(klazz);
    }


    public boolean originatesFromTrustedBundle(Object object, Set<String> trustedBundleNames, Bundle[] currentBundles) {
        final Class objectClass = object.getClass();

        if (objectClass.getClassLoader() == null) {
            return true;
        }

        if (TRUE.equals(trustedClassesCache.get(objectClass))) {
            return true;
        }

        for (Bundle bundle : currentBundles) {
            if (trustedBundleNames.contains(bundle.getSymbolicName())) {
                try {
                    final Class classInBundle = bundle.loadClass(objectClass.getCanonicalName());
                    if (objectClass.equals(classInBundle)) {
                        trustedClassesCache.put(objectClass, TRUE);
                        return true;
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
        return false;
    }
}
