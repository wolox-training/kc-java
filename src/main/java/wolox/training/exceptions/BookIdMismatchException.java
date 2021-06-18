package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookIdMismatchException extends RuntimeException{
    public BookIdMismatchException() {
        super("Book id don't match");
    }
}
