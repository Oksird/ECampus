package ua.foxminded.muzychenko;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.controller.FrontController;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SchoolApplicationFacade {

    private final FrontController frontController;

    public void runFrontController() {
        frontController.run();
    }
}
