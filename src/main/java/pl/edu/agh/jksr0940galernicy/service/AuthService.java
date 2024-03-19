package pl.edu.agh.jksr0940galernicy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private User authenticatedUser;

    public AuthService (@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean authenticate(String email) {
        log.info("Authenticating user: " + email);
        List<User> foundUsers = userRepository.findByEmail(email);
        if (foundUsers.isEmpty()) {
            log.info("Nie znaleziono adresu email: " + email);
            return false;
        }
        authenticatedUser = foundUsers.get(0);
        log.info("Zalogowano użytkownika: " + authenticatedUser.getName());
        return true;
    }

    public User getLoggedUser() {
        return authenticatedUser;
    }

    public void logout() {
        authenticatedUser = null;
    }
}
