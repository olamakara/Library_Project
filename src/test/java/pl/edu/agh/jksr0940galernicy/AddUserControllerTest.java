package pl.edu.agh.jksr0940galernicy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.jksr0940galernicy.controller.AddUserController;

public class AddUserControllerTest {

    private AddUserController controller;

    @BeforeEach
    public void setup() {
        // Replace null with a proper UserService mock.
        this.controller = new AddUserController(null, null);
    }

    @Test
    public void testValidEmail() {
        // An example of a correct email address
        String correctEmail = "test@example.com";
        Assertions.assertTrue(controller.isValidEmail(correctEmail));

    }

    @Test
    public void testInvalidEmail() {
        // An example of an incorrect email address
        String incorrectEmail = "test.example.com";
        Assertions.assertFalse(controller.isValidEmail(incorrectEmail));

    }

}