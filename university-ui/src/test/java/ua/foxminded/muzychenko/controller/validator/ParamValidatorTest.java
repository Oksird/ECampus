package ua.foxminded.muzychenko.controller.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ParamValidator.class)
class ParamValidatorTest {

    @Autowired
    private ParamValidator validator;

    @Test
    void getValidatedPageRequestShouldReturnMapWithEnteredValuesWhenValuesAreCorrect() {
        Map<String, Integer> expectedPageParams = new HashMap<>(Map.of("page", 2, "size", 10));

        assertEquals(expectedPageParams, validator.getValidatedPageRequest("2", "10"));
    }

    @Test
    void getValidatedPageRequestShouldReturnDefaultValuesWhenEnteredValuesAreWrong() {
        Map<String, Integer> expectedPageParams = new HashMap<>(Map.of("page", 1, "size", 5));

        assertEquals(expectedPageParams, validator.getValidatedPageRequest("asd", "asd"));
    }
}