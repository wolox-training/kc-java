package wolox.training.models;


import com.google.common.base.Preconditions;
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
    private String username;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthdate;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username);
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name);
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkNotNull(birthdate);
        this.birthdate = birthdate;
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

    public void removeBook(Book book) {
        books.remove(book);
    }
}
