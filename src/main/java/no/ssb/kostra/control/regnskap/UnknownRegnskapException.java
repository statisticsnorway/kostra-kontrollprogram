package no.ssb.kostra.control.regnskap;

public class UnknownRegnskapException extends Exception
{
	private static final long serialVersionUID = 1301376754272109739L;
	private String message;

    public UnknownRegnskapException (String message) {
        super (message);
        this.message = message;
    }

    public String getExceptionMessage() {
        return message;
    }
}
