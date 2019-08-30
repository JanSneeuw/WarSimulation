package nl.jansneeuw.warsimulation.validators;

public interface Validation {
    boolean isValid();
    boolean isInValid();
    String getErrorMessage();
}
