package ua.foxminded.muzychenko.view;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.controller.exception.InvalidInputException;
import ua.foxminded.muzychenko.view.util.ConsoleWriter;

import java.util.Scanner;

@Component
@AllArgsConstructor
public class ViewProvider {
    private final Scanner scanner;
    private final ConsoleWriter consoleWriter;

    public String readString() {
        try {
            return scanner.nextLine();
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    public Integer readInt() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    public void printMessage(String message) {
        consoleWriter.writeInConsole(message);
    }
}
