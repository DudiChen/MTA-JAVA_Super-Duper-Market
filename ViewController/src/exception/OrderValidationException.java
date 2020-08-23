package exception;

public class OrderValidationException extends Exception {
    private String message;
    public OrderValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
