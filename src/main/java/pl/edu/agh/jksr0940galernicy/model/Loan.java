package pl.edu.agh.jksr0940galernicy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate borrowingDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Integer fee;
    private boolean liked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY)
    private List<Reminder> reminders;

    @Override
    public String toString() {
        return "Tytuł ksiązki: " + book.getBookInfo().getTitle() + ", id ksiązki: " + book.getId() + ", data wypożyczenia: " + borrowingDate + ", data zwrotu: " + dueDate
                + ", data oddania: " + returnDate + " użytkownik " + user.getName() + " " + user.getLastName();
    }

}
