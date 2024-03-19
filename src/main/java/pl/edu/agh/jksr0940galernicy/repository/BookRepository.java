package pl.edu.agh.jksr0940galernicy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.BookCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    //by author title and category
    Page<Book> findAllByBookInfo_TitleContainingIgnoreCaseAndBookInfo_AuthorContainingIgnoreCase(String title, String author, Pageable pageable);
    Page<Book> findAllByBookInfo_TitleContainingIgnoreCaseAndBookInfo_AuthorContainingIgnoreCaseAndBookInfo_Category(String title, String author, BookCategory category, Pageable pageable);
    Page<Book> findAll(Pageable pageable);
}
