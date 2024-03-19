package pl.edu.agh.jksr0940galernicy.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.jksr0940galernicy.model.BookInfo;

import java.util.Optional;

public interface BookInfoRepository extends CrudRepository<BookInfo, Long> {
    Optional<Object> findByTitleAndAuthor(String title, String author);
}
