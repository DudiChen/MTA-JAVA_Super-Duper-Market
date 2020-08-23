package exception;

public class ProductIdNotFoundException extends Exception {
    public ProductIdNotFoundException() {}

    public ProductIdNotFoundException(String message) {
        super(message);
    }

}
