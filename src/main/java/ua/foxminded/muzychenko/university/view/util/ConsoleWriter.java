package ua.foxminded.muzychenko.university.view.util;

import lombok.AllArgsConstructor;

import java.io.PrintStream;

@AllArgsConstructor
public class ConsoleWriter {

    private PrintStream printStream;

    public void writeInConsole(String string) {
        printStream.println(string);
    }
}

