package csulb.cecs323.errorhandeling;

public class UniqueConstraintException extends Exception {

    /**
     * An exception indicating that a uniqueness constraint
     * has been violated.
     */
    public UniqueConstraintException() {
        super("Unique constraint exception thrown.");
    }

    /**
     * An exception indicating that a uniqueness constraint has
     * been violated.
     *
     * @param message The message to assign to the exception
     */
    public UniqueConstraintException(String message) {
        super(message);
    }

}
