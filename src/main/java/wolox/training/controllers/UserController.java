package wolox.training.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdDontMatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

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
    public Optional<User> findAll(@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate start, @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate end,
                                  @RequestParam(required = false, defaultValue = "") String name, @RequestParam(required = false, defaultValue = "") String username) {
        return userRepository.findAll(start, end, name, username);
    }

    /**
     * This method return one {@link User} by its id
     *
     * @param param: record in database or a username
     * @return {@link User} by id
     */
    @GetMapping("/{param}")
    @ApiOperation(value = "Giving an id, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesfully retrived user"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing th resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public User findByUser(@PathVariable("param") String param) {
        if(NumberUtils.isNumber(param)){
            return userRepository.findById(Long.parseLong(param))
                    .orElseThrow(UserNotFoundException::new);
        }else {
            return userRepository.findByUsername(param);
        }

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

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/birthdateAndName")
    public Iterable<User> findByBirthdateBetweenAndName(@RequestParam(name = "start", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate start,
                                                                            @RequestParam(name = "end", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate end,
                                                                            @RequestParam(name = "name", required = false) String name) {
        if (name != null) {
            name = name.toLowerCase();
        }
        return userRepository.findByBirthdateBetweenAndNameContainingIgnoreCase(start, end, name);
    }

}
