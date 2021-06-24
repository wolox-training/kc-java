package wolox.training.controllers;

import static java.util.Optional.ofNullable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    private Book bookTest;
    private List<Book> books;

    @MockBean
    private BookRepository mockBookRepository;

    private ObjectMapper objectMapper;

    @PostConstruct
    private void objectMapper() {
        objectMapper = new ObjectMapper();
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
        books = new ArrayList<>();

    }

    @Test
    public void whenGetAllBooks_thenReturnAListOfBooks() throws Exception {
        books.add(bookTest);
        Mockito.when(mockBookRepository.findAll()).thenReturn(books);
        mvc.perform(get("/api/books/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(books)));
    }

    @Test
    public void whenCreateABook_thenBookIsPersisted() throws Exception {
        mvc.perform(post("/api/books/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenFindABookById_thenReturnTheBook() throws Exception {
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(ofNullable(bookTest));
        mvc.perform(get("/api/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(bookTest)));
    }

    @Test
    public void whenFindABookByAuthor_thenReturnTheBook() throws Exception {
        Mockito.when(mockBookRepository.findByAuthor("DC: Alan Moore")).thenReturn(bookTest);
        mvc.perform(get("/api/books/DC: Alan Moore").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(bookTest)));
    }

    @Test
    public void whenUpdateABook_thenIsUpdated() throws Exception {
        bookTest.setTitle("Batman: la broma asesina");
        bookTest.setId(1L);
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(ofNullable(bookTest));
        mvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteABookThen_theBookIsDeleted() throws Exception {
        bookTest.setId(1L);
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(ofNullable(bookTest));
        mvc.perform(delete("/api/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
