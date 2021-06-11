package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(Integer id) {
        super("Book not found with id:" + id);
    }

    public BookNotFoundException(String author) {
        super("Book not found with author:" + author);
    }
}
