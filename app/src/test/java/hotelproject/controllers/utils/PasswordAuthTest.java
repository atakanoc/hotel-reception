package hotelproject.controllers.utils;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PasswordAuthTest {

    private final PasswordAuth passwordAuth = new PasswordAuth();
    private final String password = "password";
    private final String token = passwordAuth.hash(password);


    @Test
    public void testHash() {
        String hashCode = passwordAuth.hash(password);
        assertNotNull(hashCode);
    }

    @Test
    public void testAuthenticate() {
        assertTrue(passwordAuth.authenticate(password, token));
    }
}