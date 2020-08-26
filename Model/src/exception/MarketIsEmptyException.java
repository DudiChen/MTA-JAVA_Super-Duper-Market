package exception;

public class MarketIsEmptyException extends Exception {

    public MarketIsEmptyException(String message) {
        super(message);
    }

    public MarketIsEmptyException() {
    }
}
