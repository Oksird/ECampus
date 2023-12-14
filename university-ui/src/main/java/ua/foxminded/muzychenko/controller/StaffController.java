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
import ua.foxminded.muzychenko.dto.profile.StaffProfile;
import ua.foxminded.muzychenko.service.StaffService;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<StaffProfile> staffPage = staffService.findAll(pageCount, pageSize);

        model.addAttribute("staff", staffPage.getContent());
        model.addAttribute("currentPage", staffPage.getNumber() + 1);
        model.addAttribute("totalItems", staffPage.getTotalElements());
        model.addAttribute("totalPages", staffPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "staff/all-staff";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        StaffProfile staffProfile = staffService.findStaffByEmail(userDetails.getUsername());

        model.addAttribute("profile", staffProfile);

        return "staff/profile";
    }

    @GetMapping("/{userId}")
    public String getProfileById(@PathVariable("userId") UUID id, Model model) {
        StaffProfile profile = staffService.findStaffById(id);

        model.addAttribute("profile", profile);

        return "staff/profile";
    }
}
