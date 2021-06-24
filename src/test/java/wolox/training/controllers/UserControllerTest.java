package wolox.training.controllers;

import static java.util.Optional.ofNullable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

import javax.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    private User userTest;
    private List<User> users;
    private Book bookTest;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private BookRepository mockBookRepository;

    private ObjectMapper objectMapper;

    @PostConstruct
    private void objectMapper() {
        objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Before
    public void setUp() {
        bookTest = new Book();
        bookTest.setAuthor("DC: Alan Moore");
        bookTest.setImage("image.png");
        bookTest.setTitle("Batman: The Killing Joke");
        bookTest.setSubtitle("A subtitle");
        bookTest.setPublisher("Publisher");
        bookTest.setGenre("novela grafica");
        bookTest.setYear("1988");
        bookTest.setPages(100);
        bookTest.setIsbn("ABCD1234");
        userTest = new User();
        userTest.setUsername("marti");
        userTest.setName("Martina");
        userTest.setBirthdate(LocalDate.parse("1993-12-09"));
        userTest.addBook(bookTest);
        users = new ArrayList<User>();
    }

    @Test
    public void whenCreateAUser_thenUserIsPersisted() throws Exception {
        mvc.perform(post("/api/users/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenGetAllUsers_thenReturnAListOfUsers() throws Exception {
        users.add(userTest);
        Mockito.when(mockUserRepository.findAll()).thenReturn(users);
        mvc.perform(get("/api/users/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":null,\"username\":\"marti\",\"name\":\"Martina\",\"birthdate\":\"1993-12-09\",\"books\":[{\"id\":null,\"genre\":\"novela grafica\",\"author\":\"DC: Alan Moore\",\"image\":\"image.png\",\"title\":\"Batman: The Killing Joke\",\"subtitle\":\"A subtitle\",\"publisher\":\"Publisher\",\"year\":\"1988\",\"pages\":100,\"isbn\":\"ABCD1234\"}]}]"));
    }

    @Test
    public void whenFindAUserById_thenReturnTheUser() throws Exception {
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":null,\"username\":\"marti\",\"name\":\"Martina\",\"birthdate\":\"1993-12-09\",\"books\":[{\"id\":null,\"genre\":\"novela grafica\",\"author\":\"DC: Alan Moore\",\"image\":\"image.png\",\"title\":\"Batman: The Killing Joke\",\"subtitle\":\"A subtitle\",\"publisher\":\"Publisher\",\"year\":\"1988\",\"pages\":100,\"isbn\":\"ABCD1234\"}]}"));
    }

    @Test
    public void whenFindAUserByUsername_thenReturnTheUser() throws Exception {
        Mockito.when(mockUserRepository.findByUsername("Username")).thenReturn(userTest);
        mvc.perform(get("/api/users/Username").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":null,\"username\":\"marti\",\"name\":\"Martina\",\"birthdate\":\"1993-12-09\",\"books\":[{\"id\":null,\"genre\":\"novela grafica\",\"author\":\"DC: Alan Moore\",\"image\":\"image.png\",\"title\":\"Batman: The Killing Joke\",\"subtitle\":\"A subtitle\",\"publisher\":\"Publisher\",\"year\":\"1988\",\"pages\":100,\"isbn\":\"ABCD1234\"}]}"));
    }

    @Test
    public void whenUpdateAUser_thenTheUserIsUpdated() throws Exception {
        userTest.setName("Other name");
        userTest.setId(1L);
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userTest)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteAUser_thenTheUserIsDeleted() throws Exception {
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteAUserDontExists_thenReturnNotFound() throws Exception {
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(delete("/api/users/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenAddABookToUsers_thenIsPersistedInTheUserList() throws Exception {
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(ofNullable(bookTest));
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(put("/api/users/1/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenAddABookToAUsersThatNotExists_thenReturnNotFound() throws Exception {
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(ofNullable(bookTest));
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(put("/api/users/2/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenDeleteABook_thenIsPersistedInTheUserList() throws Exception {
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(ofNullable(bookTest));
        Mockito.when(mockUserRepository.findById(1L)).thenReturn(ofNullable(userTest));
        mvc.perform(delete("/api/users/1/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andExpect(status().isOk());
    }
}
