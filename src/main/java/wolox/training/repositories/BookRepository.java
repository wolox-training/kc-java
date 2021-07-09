package wolox.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE (:publisher IS NULL OR b.publisher LIKE %:publisher)"
            + " AND (:genre IS NULL OR b.genre LIKE %:genre%)"
            + " AND (:year IS NULL OR b.year = :year)")
    List<Book> findByPublisherAndGenreAndYear(@Param("publisher") String publisher, @Param("genre") String genre, @Param("year") String year);

    @Query("SELECT b FROM Book b WHERE (:publisher IS NULL OR b.publisher LIKE %:publisher)"
            + " AND (:title IS NULL OR b.title LIKE %:author%)"
            + " AND (:title IS NULL OR b.title LIKE %:title%)"
            + " AND (:subtitle IS NULL OR b.subtitle LIKE %:subtitle%)"
            + " AND (:genre IS NULL OR b.genre LIKE %:genre%)"
            + " AND (:year IS NULL OR b.year = :year)"
            + " AND (:isbn IS NULL OR b.isbn = :isbn)")
    Optional<Book> findAll(@Param("author") String author, @Param("publisher") String publisher, @Param("genre") String genre, @Param("year") String year, @Param("title") String title, @Param("subtitle") String subtitle, @Param("isbn") String isbn);

}
