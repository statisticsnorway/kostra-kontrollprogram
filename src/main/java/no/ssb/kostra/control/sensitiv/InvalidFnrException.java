package no.ssb.kostra.control.sensitiv;
public class InvalidFnrException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;

    public InvalidFnrException(String message) {
        super(message);
        this.message = message;
    }

    public String getExceptionMessage() {
        return message;
    }
}
