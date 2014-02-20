package fr.inria.amazones.logosng.interception;

public class HashValueHandler implements ValueHandler {

    public Object handleValue(Object value) {
        return "#(hashed_value: " + value.hashCode() + ")";
    }
}
