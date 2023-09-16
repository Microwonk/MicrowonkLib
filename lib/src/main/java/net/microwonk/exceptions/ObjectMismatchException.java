package net.microwonk.exceptions;

public class ObjectMismatchException extends RuntimeException {
    public ObjectMismatchException() {
    }

    public ObjectMismatchException(String message) {
        super(message);
    }

    public ObjectMismatchException(Object should, Object given) {
        super(given.getClass() + " given, " + should.getClass() + " needed");
    }
}
