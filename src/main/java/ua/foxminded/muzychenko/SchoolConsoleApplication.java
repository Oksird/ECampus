package ua.foxminded.muzychenko;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolConsoleApplication implements CommandLineRunner {

    private final SchoolApplicationFacade schoolApplicationFacade;

    public SchoolConsoleApplication(SchoolApplicationFacade schoolApplicationFacade) {
        this.schoolApplicationFacade = schoolApplicationFacade;
    }

    public static void main(String[] args) {
        SpringApplication.run(SchoolConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        schoolApplicationFacade.runFrontController();
    }
}
