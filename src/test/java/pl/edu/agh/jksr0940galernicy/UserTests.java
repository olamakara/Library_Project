package pl.edu.agh.jksr0940galernicy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.jksr0940galernicy.controller.AddUserController;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.model.UserType;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class UserTests {



    private User user;

    private final String name = "Anna";
    private final String lastName = "Nowak";
    private final String email = "anna.nowak@gmail.com";

    private final UserType userType = UserType.ADMIN;

    @BeforeEach
    public void setupTestData() {
        user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUserType(userType);

    }

    @Test
    @DisplayName("Test name is correctly set")
    public void testName() {
        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Test last name is correctly set")
    public void testLastName() {
        assertThat(user.getLastName()).isEqualTo(lastName);
    }

    @Test
    @DisplayName("Test email is correctly set")
    public void testEmail() {
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Test user type is correctly set")
    public void testUserType() {
        assertThat(user.getUserType()).isEqualTo(userType);
    }


}
