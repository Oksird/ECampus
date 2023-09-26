package ua.foxminded.muzychenko.service.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PasswordEncoder.class)
class PasswordEncoderTest {

    @MockBean
    private BCryptPasswordEncoder encoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void encodeShouldReturnEncodedPassword() {
        String pass = "pass";
        String encodedPass = "enqPass123";

        when(encoder.encode(pass)).thenReturn(encodedPass);

        assertEquals(encodedPass, passwordEncoder.encode(pass));
    }

    @Test
    void matchesShouldReturnTrueIfPasswordMatcheHashedPass() {
        String pass = "pass";
        String encodedPass = "enqPass123";

        when(encoder.matches(pass, encodedPass)).thenReturn(true);

        assertTrue(passwordEncoder.matches(pass, encodedPass));
    }
}