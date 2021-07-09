package wolox.training.controllers;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;


    @Qualifier(value = "OpenLibraryService")
    private OpenLibraryService openLibraryService;


    /**
     * This method return all books registers
     *
     * @return all books
     */
    @GetMapping
    public Optional<Book> findAll(@RequestParam(required = false) String author,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String subtitle,
                              @RequestParam(required = false) String publisher,
                              @RequestParam(required = false) String genre,
                              @RequestParam(required = false) String year,
                                  @RequestParam(required = false) String isbn) {
        return bookRepository.findAll(author, publisher, genre, year, title, subtitle, isbn);
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

    @GetMapping("/search")
    public ResponseEntity<Book> search(@RequestParam(name = "isbn", required = false) String isbn)
            throws IOException {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalBook.get());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    openLibraryService.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn)));
        }
    }

    @GetMapping("/publisherAndGenreAndYear")
    public Iterable<Book> findByPublisherAndGenreAndYear(@RequestParam(name= "publisher", required = false) String publisher,
                                                         @RequestParam(name="genre", required=false) String genre,
                                                         @RequestParam(name="year", required = false) String year) {
        return bookRepository.findByPublisherAndGenreAndYear(publisher, genre, year);
    }


}
