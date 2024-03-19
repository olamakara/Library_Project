package pl.edu.agh.jksr0940galernicy.service;

import javafx.collections.FXCollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.BookCategory;
import pl.edu.agh.jksr0940galernicy.model.BookInfo;
import pl.edu.agh.jksr0940galernicy.repository.BookInfoRepository;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookInfoRepository bookInfoRepository;

    public BookService(
            @Autowired BookRepository bookRepository,
            @Autowired BookInfoRepository bookInfoRepository) {
        this.bookRepository = bookRepository;
        this.bookInfoRepository = bookInfoRepository;
    }

    public void addBook(String title, String author, String coverImage, String contents, BookCategory category) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setCategory(category);

        var existingBookInfo = bookInfoRepository.findByTitleAndAuthor(title, author);
        if (existingBookInfo.isPresent()) {
            bookInfo = (BookInfo) existingBookInfo.get();
        }
        else {
            bookInfoRepository.save(bookInfo);
        }

        Book book = new Book();
        book.setBookInfo(bookInfo);
        book.setCoverImage(coverImage);
        book.setContents(contents);
        book.setAvailable(true);

        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<String> getAllAuthors() {
        return getAllBooks().stream()
                .map(el -> el.getBookInfo().getAuthor())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Book> getFilteredBooks(String title, String author, BookCategory category) {
       return getAllBooks().stream().filter(book -> {
           boolean titleMatch = title == null || book.getBookInfo().getTitle().toLowerCase().contains(title.toLowerCase());
           boolean authorMatch = author == null || book.getBookInfo().getAuthor().toLowerCase().contains(author.toLowerCase());
           boolean categoryMatch = category == null || book.getBookInfo().getCategory().equals(category);
           return titleMatch && authorMatch && categoryMatch;
       }).collect(Collectors.toList());
    }

    public int getNumberOfBooks() {
        return (int) bookRepository.count();
    }

    public int getNumberOfBooks(String title, String author, BookCategory category) {
        PageRequest pageable = PageRequest.of(0, 10000);
        if (author == null) {
            author = "";
        }
        if (title == null) {
            title = "";
        }
        if (category == null) {
            Page<Book> bookPage = bookRepository.findAllByBookInfo_TitleContainingIgnoreCaseAndBookInfo_AuthorContainingIgnoreCase(title, author, pageable);
            List<Book> books = bookPage.getContent();
            return books.size();
        }
        else{
            Page<Book> bookPage = bookRepository.findAllByBookInfo_TitleContainingIgnoreCaseAndBookInfo_AuthorContainingIgnoreCaseAndBookInfo_Category(title, author, category, pageable);
            List<Book> books = bookPage.getContent();
            return books.size();
        }
    }

    public List<Book> getPaginatedBooks(int page, int size, String sortType, String title, String author, BookCategory category) {
        Sort sort = switch (sortType) {
            case "TITLE_ASC" -> Sort.by("bookInfo.title").ascending();
            case "TITLE_DESC" -> Sort.by("bookInfo.title").descending();
            case "POPULARITY_DESC" -> Sort.by("popularity").descending();
            case "POPULARITY_ASC" -> Sort.by("popularity").ascending();
            case "RATE_DESC" -> Sort.by("rate").descending();
            case "RATE_ASC" -> Sort.by("rate").ascending();
            default -> Sort.unsorted();
        };


        PageRequest pageable = PageRequest.of(page, size, sort);
        if (author == null) {
            author = "";
        }
        if (title == null) {
            title = "";
        }
        if (category == null) {
            Page<Book> bookPage = bookRepository.findAllByBookInfo_TitleContainingIgnoreCaseAndBookInfo_AuthorContainingIgnoreCase(title, author, pageable);
            List<Book> books = bookPage.getContent();

            return FXCollections.observableList(books);
        }
        else{
            Page<Book> bookPage = bookRepository.findAllByBookInfo_TitleContainingIgnoreCaseAndBookInfo_AuthorContainingIgnoreCaseAndBookInfo_Category(title, author, category, pageable);
            List<Book> books = bookPage.getContent();
            return FXCollections.observableList(books);
        }

    }


}
