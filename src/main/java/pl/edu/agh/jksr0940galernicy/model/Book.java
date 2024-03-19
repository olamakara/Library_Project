package pl.edu.agh.jksr0940galernicy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.util.List;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String coverImage;
    private String contents;
    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id")
    private BookInfo bookInfo;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Loan> loans;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    ///get number of loans
    @Formula("(SELECT COUNT(*) FROM Loans l WHERE l.book_id = id)")
    private int popularity;

    @Formula("(SELECT COALESCE(AVG(r.rate), 0) FROM Reviews r WHERE r.book_id = id)")
    private double rate;

    public int getPopularity() {
        return popularity;
    }

    ///get average rate
    public double getRate() {
        return rate;
    }


}
