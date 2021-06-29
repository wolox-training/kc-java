package wolox.training.shared;

import wolox.training.dto.BookDTO;
import wolox.training.models.Book;

public class ConvertDto {

    public Book convertDtoToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setAuthor(bookDTO.getAuthors());
        book.setImage("");
        book.setTitle(bookDTO.getTitle());
        book.setSubtitle(bookDTO.getSubtitle());
        book.setPublisher(bookDTO.getPublishers());
        book.setYear(bookDTO.getPublishDate());
        book.setPages(bookDTO.getNumberOfPages());
        book.setIsbn(bookDTO.getIsbn());
        return book;
    }

}
