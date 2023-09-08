package ua.foxminded.muzychenko.university.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.foxminded.muzychenko.university.view.util.ConsoleWriter;

import java.util.Scanner;

@Configuration
@ComponentScan("ua.foxminded.muzychenko")
public class AppConfiguration {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public ConsoleWriter consoleWriter() {
        return new ConsoleWriter(System.out);
    }

}
