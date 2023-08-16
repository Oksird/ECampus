package ua.foxminded.muzychenko;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.foxminded.muzychenko.config.AppConfiguration;

public class SchoolConsoleApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfiguration.class);
        SchoolApplicationFacade schoolApplicationFacade
            = context.getBean("schoolApplicationFacade", SchoolApplicationFacade.class);
        schoolApplicationFacade.runFrontController();
    }
}
