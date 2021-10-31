package csulb.cecs323.errorhandeling;

public class UniqueConstraintException extends Exception {

    public UniqueConstraintException() {
        super("Unique constraint exception thrown.");
    }

    public UniqueConstraintException(String message) {
        super(message);
    }

}
