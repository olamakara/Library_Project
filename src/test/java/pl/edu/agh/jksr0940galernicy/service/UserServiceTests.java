package pl.edu.agh.jksr0940galernicy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.model.UserType;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    String name = "Ann";
    String lastName = "Nowak";
    String email = "anna.nowak2@gmail.com";
    UserType userType = UserType.ADMIN;

    private User user;

    List<User> users;

    @BeforeEach
    public void setupTestData() {
        userService = new UserService(userRepository);

        user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUserType(userType);

        users = new ArrayList<>();
        users.add(user);

        lenient().when(userRepository.findByEmail(email)).thenAnswer(invocation -> {
            String queriedEmail = invocation.getArgument(0);
            if (queriedEmail.equals(email)) {
                return users;
            } else {
                return Collections.emptyList();
            }
        });
        lenient().when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(16L);
                    users.add(user);
                    return user;
                });
        lenient().when(userRepository.findByName(name)).thenReturn(Collections.singletonList(user));

    }

    @Test
    @DisplayName("Test repository")
    public void testRepository() {
        List<User> usersRepository = userRepository.findByEmail(email);

        assertFalse(usersRepository.isEmpty());
        User userFromRepository = usersRepository.get(0);
        assertEquals(email, userFromRepository.getEmail());
    }

    @Test
    @DisplayName("Test repository returns correct user by email")
    public void isUserPresentTest() {
        Mono<User> userMono = userService.isUserPresent(email);
        StepVerifier.create(userMono)
                .expectNextMatches(user -> user.getEmail().equals(email))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test repository adds a user")
    public void addUser() {
        var tempName = "Test";
        var tempLastName = "LastTest";
        var tempEmail = "test@gmail.com";
        var tempUserType = UserType.USER;


        StepVerifier.create(userService.addUser(
                        tempName, tempLastName, tempEmail, tempUserType))
                .expectSubscription()
                .expectNextMatches(
                        user -> user.getEmail().equals(tempEmail) &&
                                user.getName().equals(tempName) &&
                                user.getLastName().equals(tempLastName) &&
                                user.getUserType().equals(tempUserType)
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Test prevent adding duplicate user")
    public void addDuplicateUser() {
        when(userRepository.findByEmail(email)).thenReturn(users);

        StepVerifier.create(userService.addUser(name, lastName, email, userType))
                .expectSubscription()
                .expectError()
                .verify();

    }


}

