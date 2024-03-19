package pl.edu.agh.jksr0940galernicy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    public BookCategory getFavouriteCategory() {
        int[] categories = new int[BookCategory.values().length];
        for (Loan loan : loans) {
            categories[loan.getBook().getBookInfo().getCategory().ordinal()]++;
        }
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < categories.length; i++) {
            if (categories[i] > max) {
                max = categories[i];
                maxIndex = i;
            }
        }
        return BookCategory.values()[maxIndex];
    }
}
