package pl.edu.agh.jksr0940galernicy.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.BookCategory;
import pl.edu.agh.jksr0940galernicy.service.BookService;

import java.util.Arrays;

@Component
public class DataFeeder {

    private BookService bookService;

    public DataFeeder(@Autowired  BookService bookService) {
        this.bookService = bookService;
    }

    private static String[] titles = {
            "The Lord of the Rings",
            "The Hobbit",
            "Harry Potter and the Philosopher's Stone",
            "Harry Potter and the Chamber of Secrets",
            "Harry Potter and the Prisoner of Azkaban",
            "Harry Potter and the Goblet of Fire",
            "Harry Potter and the Order of the Phoenix",
            "Harry Potter and the Half-Blood Prince",
            "Spider Man",
            "Scooby Doo"
    };

    private static String[] authors = {
            "J.R.R. Tolkien",
            "Harry Potter",
            "J.K. Rowling",
            "Stan Lee",
            "Scooby Doo"
    };

    private static BookCategory[] categories = BookCategory.values();

    private static String[] urls = {
            "https://m.media-amazon.com/images/I/91pI+R+GE7L._AC_UF1000,1000_QL80_.jpg",
            "https://images.booksense.com/images/403/353/9780590353403.jpg",
            "https://bonito.pl/cache/9/58-harry-potter-book-7-harry_400.jpg",
    };

    public void feed() {
        int booksCount = 50;

        for(int i = 0; i < booksCount; i++) {
            String title = titles[(int) (Math.random() * titles.length)];
            String author = authors[(int) (Math.random() * authors.length)];
            BookCategory category = categories[(int) (Math.random() * categories.length)];
            String url = urls[(int) (Math.random() * urls.length)];
            String contents = "Test test test";


            bookService.addBook(title, author, url, contents, category);
        }
    }
}
