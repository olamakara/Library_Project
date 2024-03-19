package pl.edu.agh.jksr0940galernicy.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.model.UserType;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> isUserPresent(String email) {
       return Mono.fromCallable(() -> userRepository.findByEmail(email))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(users -> {
                    log.info("Users with email " + email + ": " + users.size());
                    users.forEach(user -> log.info(user.toString()));
                     if (users.isEmpty()) {
                          return Mono.empty();
                     }
                     return Mono.just(users.get(0));
                });
    }

    public Mono<User> isUserPresent(User user) {
        return isUserPresent(user.getEmail());
    }

    @Transactional
    public Mono<User> addUser(String name, String lastName, String email, UserType userType) {
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUserType(userType);

        return Mono.just(user)
                .flatMap(this::isUserPresent)
                .hasElement()
                .flatMap(isPresent -> {
                    if (isPresent) {
                        return Mono.error(new Exception("Użytkownik o adresie " + email + " istnieje"));
                    }
                    return Mono.just(user);
                })
                .flatMap(this::saveUserToDB)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<User> saveUserToDB(User user) {
        return Mono.fromCallable(() -> userRepository.save(user))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
