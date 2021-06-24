package wolox.training.controllers;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * This method return all books registers
     *
     * @return all books
     */
    @GetMapping
    public Iterable findAll() {
        return bookRepository.findAll();
    }

    /**
     * This method return one {@link Book} for author name
     *
     * @param param: author name or record in database
     * @return {@link Book} by author
     */
    @GetMapping("/{param}")
    public Book findByParam(@PathVariable String param) {
        if(NumberUtils.isNumber(param)){
            return bookRepository.findById(Long.parseLong(param))
                    .orElseThrow(() -> new BookNotFoundException(Long.parseLong(param)));
        }else{
            return bookRepository.findByAuthor(param);
        }
    }

    /**
     * This method creates an {@link Book} with following parameters
     *
     * @param book: book for create
     * @return book by body book
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * This method deletes an book for id record
     *
     * @param id: book id by delete
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.deleteById(id);
    }

    /**
     * This method replaces an {@link Book} with following parameters
     *
     * @param book: book to save
     * @param id: id to search
     * @return book update
     */
    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return bookRepository.save(book);
    }

}
