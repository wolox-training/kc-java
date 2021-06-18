package wolox.training.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdDontMatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * This method return all users registers
     *
     * @return all users
     */
    @GetMapping
    public Iterable findAll() {
        return userRepository.findAll();
    }

    /**
     * This method return one {@link User} by its username
     *
     * @param username: username
     * @return {@link User} by username
     */
    @GetMapping("/{username}")
    public User findByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * This method return one {@link User} by its id
     *
     * @param id: record in database
     * @return {@link User} by id
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an id, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesfully retrived user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing th resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * This method creates an {@link User} with following parameters
     *
     * @param user: user for create
     * @return book by body user
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * This method deletes an book by its id
     *
     * @param id: user id by delete
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    /**
     * This method replaces an {@link User} with following parameters
     *
     * @param user: user to save
     * @param id: id to search
     * @return user update
     */
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdDontMatchException();
        }
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    /**
     * This method add an {@link Book} with following parameters
     *
     * @param book: book to add
     * @param id: id to search
     * @return user with to added book
     */
    @PutMapping("/{id}/books")
    @ResponseStatus(HttpStatus.CREATED)
    public User addBook(@PathVariable Long id, @RequestBody Book book)
            throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.addBook(book);
        return userRepository.save(user);
    }

    /**
     * This method delete an {@link Book} with following parameters
     *
     * @param book: book to delete
     * @param id: id to search
     * @return user with to deleted book
     */
    @DeleteMapping("/{id}/books")
    public User deleteBook(@PathVariable Long id, @RequestBody Book book)
            throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.removeBook(book);
        return userRepository.save(user);
    }

}
