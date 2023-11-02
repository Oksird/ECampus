package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.foxminded.muzychenko.dto.request.UserRegistrationRequest;
import ua.foxminded.muzychenko.service.PendingUserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final PendingUserService pendingUserService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("registrationRequest") UserRegistrationRequest request) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationRequest") UserRegistrationRequest request) {
        pendingUserService.register(request);
        return "redirect:/login";
    }
}
