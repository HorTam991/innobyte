package hu.tamas.airlines.util.exception;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = 7248064463626087124L;

    public NotFoundException(String message) {
        super(message);
    }

}
