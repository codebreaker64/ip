package devin.exception;

//Solution inspired by https://www.geeksforgeeks.org/user-defined-custom-exception-in-java/
public class DevinException extends Exception {

    /**
     * Constructs a new instance of DevinException with the specified message.
     *
     * @param message error message.
     */
    public DevinException(String message) {
        super(message);
    }
}
