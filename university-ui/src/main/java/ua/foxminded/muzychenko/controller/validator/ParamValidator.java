package ua.foxminded.muzychenko.controller.validator;

import ua.foxminded.muzychenko.config.Validator;

import java.util.HashMap;
import java.util.Map;

@Validator
public class ParamValidator {

    public Map<String, Integer> getValidatedPageRequest(String page, String size) {

        Map<String, Integer> pageParams = new HashMap<>(Map.of("page", 1, "size", 5));

        try {
            pageParams.put("page", Integer.parseInt(page));
            pageParams.put("size", Integer.parseInt(size));
        } catch (NumberFormatException ignored) {
            //TODO: create message in logs
        }

        return pageParams;
    }
}
