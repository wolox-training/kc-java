package wolox.training.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import wolox.training.dto.BookDTO;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.shared.BuildDto;
import wolox.training.shared.ConvertDto;

@Component("OpenLibraryService")
public class OpenLibraryService {

    @Value("${openlibrary.base.url}")
    private String baseUrl;

    private ObjectMapper mapper;
    private RestTemplate restTemplate;
    private ConvertDto convertDto;
    private BuildDto buildDto;

    @PostConstruct
    private void mapper() {
        mapper = new ObjectMapper();
        restTemplate = new RestTemplate();
    }

    @Autowired
    private BookRepository bookRepository;

    public Optional<Book> findByIsbn(String isbn) throws IOException {
        Optional<BookDTO> optionalBookDTO = bookInfo(isbn);
        return optionalBookDTO.map(bookDTO -> {
            Book book = convertDto.convertDtoToEntity(bookDTO);
            return bookRepository.save(book);
        });
    }

    public Optional<BookDTO> bookInfo(String isbn) throws IOException {
        String urlString = baseUrl + String.format("?bibkeys=ISBN:%s&format=json&jscmd=data", isbn);
        ResponseEntity<String> response = restTemplate.getForEntity(urlString, String.class);
        JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        if (root.size() > 0) {
            return Optional.of(buildDto.buildDto(isbn, root));
        } else {
            return Optional.empty();
        }
    }
}
