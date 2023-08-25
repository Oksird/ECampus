package ua.foxminded.muzychenko.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.controller.exception.InvalidInputException;
import ua.foxminded.muzychenko.view.util.ConsoleWriter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class ViewProviderTest {

    @MockBean
    private Scanner scanner;
    @MockBean
    private ConsoleWriter consoleWriter;
    @Autowired
    private ViewProvider viewProvider;

    @Test
    void readStringShouldReturnEnteredString() {
        String expectedString = "String";
        when(scanner.nextLine())
            .thenReturn("String");
        assertEquals(expectedString, viewProvider.readString());
    }

    @Test
    void readStringShouldThrowExceptionWhenInputIsIncorrect() {
        when(scanner.nextLine())
            .thenThrow(InvalidInputException.class);
        assertThrows(InvalidInputException.class, () -> viewProvider.readString());
    }

    @Test
    void readIntShouldReturnEnteredInteger() {
        int expectedInt = 1;
        when(scanner.nextInt())
            .thenReturn(1);
        assertEquals(expectedInt, viewProvider.readInt());
    }

    @Test
    void readIntShouldThrowExceptionWhenInputIsIncorrect() {
        when(scanner.nextInt())
            .thenThrow(InvalidInputException.class);
        assertThrows(InvalidInputException.class, () -> viewProvider.readInt());
    }

    @Test
    void printMessageShouldPrintToConsole() {
        doNothing().when(consoleWriter).writeInConsole(any(String.class));

        String message = "Test message";
        viewProvider.printMessage(message);

        verify(consoleWriter).writeInConsole(any(String.class));

    }
}