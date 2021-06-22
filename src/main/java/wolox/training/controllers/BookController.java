package wolox.training.controllers;

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
     * @param authorTitle: author name
     * @return {@link Book} by author
     */
    @GetMapping("/author/{author}")
    public Book findByAuthor(@PathVariable String authorTitle) {
        return bookRepository.findByAuthor(authorTitle);
    }

    /**
     * This method return one {@link Book} for id
     *
     * @param id: record in database
     * @return {@link Book} by id
     */
    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
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
