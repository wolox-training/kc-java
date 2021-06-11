package wolox.training.exceptions;

public class BookIdMismatchException extends RuntimeException{
    public BookIdMismatchException() {
        super("The id was not obtained");
    }
}
