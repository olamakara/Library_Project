package pl.edu.agh.jksr0940galernicy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.jksr0940galernicy.model.BookCategory;
import pl.edu.agh.jksr0940galernicy.model.BookInfo;
import pl.edu.agh.jksr0940galernicy.repository.BookInfoRepository;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import pl.edu.agh.jksr0940galernicy.model.Book;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookInfoRepository bookInfoRepository;

    @Mock
    BookInfo mockBookInfo;

    BookService bookService;

    @BeforeEach
    void setUp() {
        this.bookService = new BookService(bookRepository, bookInfoRepository);
    }

    @Test
    void addBook_BookInfoNotExisting() {
        // given
        when(bookInfoRepository.findByTitleAndAuthor(any(String.class), any(String.class))).thenReturn(Optional.empty());
        // when
        bookService.addBook("title", "author", "coverImage", "contents", BookCategory.CHILDREN);
        // then
        Mockito.verify(bookInfoRepository).save(any(BookInfo.class));
        Mockito.verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBook_BookInfoExisting() {
        // given
        when(bookInfoRepository.findByTitleAndAuthor(any(String.class), any(String.class))).thenReturn(Optional.of(mockBookInfo));
        // when
        bookService.addBook("title", "author", "coverImage", "contents", BookCategory.BIOGRAPHY);
        // then
        Mockito.verify(bookInfoRepository, Mockito.never()).save(any(BookInfo.class));
        Mockito.verify(bookRepository).save(any(Book.class));
    }
}
