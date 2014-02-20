package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logosng.logging.ServiceLoggingEvent;

public class NoLogValueHandler implements ValueHandler {

    public Object handleValue(Object value) {
        return ServiceLoggingEvent.NO_LOG_PLACEHOLDER;
    }
}
