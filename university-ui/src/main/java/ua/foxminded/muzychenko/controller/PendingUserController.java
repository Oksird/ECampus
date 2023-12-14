package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.PendingUserProfile;
import ua.foxminded.muzychenko.service.PendingUserService;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pending-users")
public class PendingUserController {

    private final PendingUserService pendingUserService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<PendingUserProfile> pendingUsersPage = pendingUserService.findAll(pageCount, pageSize);

        model.addAttribute("pendingUsers", pendingUsersPage.getContent());
        model.addAttribute("currentPage", pendingUsersPage.getNumber() + 1);
        model.addAttribute("totalItems", pendingUsersPage.getTotalElements());
        model.addAttribute("totalPages", pendingUsersPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "pendingUser/pending-users";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        PendingUserProfile pendingUserProfile = pendingUserService.findByEmail(userDetails.getUsername());

        model.addAttribute("profile", pendingUserProfile);

        return "pendingUser/profile";
    }

    @GetMapping("/{userId}")
    public String getProfileById(@PathVariable("userId") UUID id, Model model) {
        PendingUserProfile profile = pendingUserService.findById(id);

        model.addAttribute("profile", profile);

        return "pendingUser/profile";
    }
}
