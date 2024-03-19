package pl.edu.agh.jksr0940galernicy.model;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "bookinfo")
@Data
public class BookInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String author;

    @Enumerated(EnumType.STRING)
    private BookCategory category;

}
