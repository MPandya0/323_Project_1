package csulb.cecs323.errorhandeling;

public class PrimaryKeyConstraintException extends Exception {

    public PrimaryKeyConstraintException() {
        super("Primary key constraint exception thrown");
    }

    public PrimaryKeyConstraintException(String message) {
        super(message);
    }

}
