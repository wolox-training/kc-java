package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookIdMismatchException extends RuntimeException{
    public BookIdMismatchException() {
        super("The id was not obtained");
    }
}
