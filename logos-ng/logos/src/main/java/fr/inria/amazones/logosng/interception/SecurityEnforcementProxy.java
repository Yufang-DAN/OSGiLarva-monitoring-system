package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logosng.storage.AppendOnlyCompressedCryptedEventStore;
import fr.inria.amazones.logosng.logging.LogosSecurityException;
import fr.inria.amazones.logosng.logging.ServiceLoggingEvent;

import org.osgi.framework.ServiceReference;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class SecurityEnforcementProxy implements InvocationHandler {

    private final Object service;
    private final ServiceReference serviceReference;
    private final AppendOnlyCompressedCryptedEventStore eventLogger;

    public SecurityEnforcementProxy(Object service, ServiceReference serviceReference, AppendOnlyCompressedCryptedEventStore eventLogger) {
        this.service = service;
        this.serviceReference = serviceReference;
        this.eventLogger = eventLogger;
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        final LogosSecurityException securityException = new LogosSecurityException(
                format("Call to %s on %s is refused for security reasons by logos-ng.",
                method.getName(),
                service.getClass().getCanonicalName()));
        this.eventLogger.log(ServiceLoggingEvent.exceptionEvent(method, serviceReference, args == null ? null : asList(args), securityException));
        throw securityException;
    }
}
