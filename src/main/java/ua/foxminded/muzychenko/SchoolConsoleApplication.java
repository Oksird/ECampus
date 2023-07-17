package ua.foxminded.muzychenko;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SchoolConsoleApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfiguration.class);
        SchoolApplicationFacade schoolApplicationFacade
            = context.getBean("schoolApplicationFacade", SchoolApplicationFacade.class);
        schoolApplicationFacade.runQueries();
    }
}
