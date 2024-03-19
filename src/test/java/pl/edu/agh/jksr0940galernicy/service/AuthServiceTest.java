package pl.edu.agh.jksr0940galernicy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {


    private AuthService authService;

    @Mock
    UserRepository userRepository;
    @Mock
    private User user;
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        user = Mockito.mock(User.class);
    }

    @Test
    void authenticate_success() {
        String email = "test@test.com";
        Mockito.when(userRepository.findByEmail(any())).thenReturn(List.of(user));
        authService = new AuthService(userRepository);
        assertTrue(authService.authenticate(email));
    }

    @Test
    void authenticate_fail() {
        String email = "test@test.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(List.of());
        authService = new AuthService(userRepository);
        assertFalse(authService.authenticate(email));
    }

    @Test
    void getLoggedUser() {
        String email = "test@test.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(java.util.List.of(user));
        authService = new AuthService(userRepository);
        authService.authenticate(email);
        assertEquals(user, authService.getLoggedUser());
    }
}