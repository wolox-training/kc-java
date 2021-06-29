package wolox.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.models.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);
}
