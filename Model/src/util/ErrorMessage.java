package util;

public class ErrorMessage {
//    private String message;
    private StringBuilder messageBuilder;
    private String header = "Errors: ";

//    public ErrorMessage(String message) {
//        this.message = message;
//    }

    public ErrorMessage() {
        this.messageBuilder = new StringBuilder();
    }

    public ErrorMessage(String message) {
        this.messageBuilder = new StringBuilder(message);
    }


    public String getMessage() {
        return this.header + System.lineSeparator() + this.messageBuilder.toString();
    }

    public void setMessage(String message) {
        this.messageBuilder = new StringBuilder(message);
    }

    public void appendMessage(String message) { this.messageBuilder.append(message); }

//    @Override
//    public String toString() {
//        return "ErrorMessage{" +
//                "message='" + message + '\'' +
//                '}';

    @Override
    public String toString() {
        return this.getMessage();
    }
}
