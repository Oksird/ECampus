package ua.foxminded.muzychenko.university;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.university.controller.FrontController;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UniversityApplicationFacade {

    private final FrontController frontController;

    public void runFrontController() {
        frontController.run();
    }
}
