package wolox.training.models;


import com.sun.istack.NotNull;
import wolox.training.exceptions.BookAlreadyOwnedException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity()
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String userName;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "booksUsers",
            joinColumns = @JoinColumn(name = "bookId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userId",
                    referencedColumnName = "id"))
    private List<Book> books;

    public User(){
        this.books = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void addBook(Book book) throws BookAlreadyOwnedException {
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }
        books.add(book);
    }
}
