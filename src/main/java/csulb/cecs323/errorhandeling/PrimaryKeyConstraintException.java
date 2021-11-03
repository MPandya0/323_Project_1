package csulb.cecs323.errorhandeling;

public class PrimaryKeyConstraintException extends Exception {

    /**
     * An exception indicating that a primary key constraint
     * has been violated.
     */
    public PrimaryKeyConstraintException() {
        super("Primary key constraint exception thrown");
    }

    /**
     * An exception indicating that a primary key constraint
     * has been violated.
     * @param message The message assigned to the exception.
     */
    public PrimaryKeyConstraintException(String message) {
        super(message);
    }
}
