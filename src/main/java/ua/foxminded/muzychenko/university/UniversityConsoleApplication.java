package ua.foxminded.muzychenko.university;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UniversityConsoleApplication implements CommandLineRunner {

    private final UniversityApplicationFacade universityApplicationFacade;

    public UniversityConsoleApplication(UniversityApplicationFacade universityApplicationFacade) {
        this.universityApplicationFacade = universityApplicationFacade;
    }

    public static void main(String[] args) {
        SpringApplication.run(UniversityConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        universityApplicationFacade.runFrontController();
    }
}
