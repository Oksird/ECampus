package ua.foxminded.muzychenko.view.util;

import org.springframework.stereotype.Component;

import java.io.Console;
import java.io.PrintWriter;

@Component
public class ConsoleWriter {
    private final Console console = System.console();

    public void writeInConsole(String string) {
        try (PrintWriter writer = console.writer()) {
            writer.println(string);
        }
    }
}

