package ua.foxminded.muzychenko.service.util;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordEncoder {
    private final BCryptPasswordEncoder encoder;

    public String encode(String password) {
        return encoder.encode(password);
    }

    public boolean matches(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
