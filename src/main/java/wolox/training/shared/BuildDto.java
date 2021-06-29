package wolox.training.shared;

import com.fasterxml.jackson.databind.JsonNode;
import wolox.training.dto.BookDTO;

public class BuildDto {

    public BookDTO buildDto(String isbn, JsonNode root) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn(isbn);
        bookDTO.setTitle(getValueFromJsonNode(root, "title"));
        bookDTO.setSubtitle(getValueFromJsonNode(root, "subtitle"));
        bookDTO.setPublishers(getValueFromJsonNode(root, "publishers"));
        bookDTO.setPublishDate(getValueFromJsonNode(root, "publish_date"));
        bookDTO.setNumberOfPages(Integer.parseInt(getValueFromJsonNode(root, "number_of_pages")));
        bookDTO.setAuthors(root.findPath("authors").findPath("name").textValue());
        return bookDTO;
    }

    private String getValueFromJsonNode(JsonNode root, String key) {
        return String.join(", ", root.findValuesAsText(key));
    }
}
