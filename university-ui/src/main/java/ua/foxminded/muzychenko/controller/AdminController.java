package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.profile.AdminProfile;
import ua.foxminded.muzychenko.service.AdminService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<AdminProfile> adminPage = adminService.findAll(pageCount, pageSize);

        model.addAttribute("admins", adminPage.getContent());
        model.addAttribute("currentPage", adminPage.getNumber() + 1);
        model.addAttribute("totalItems", adminPage.getTotalElements());
        model.addAttribute("totalPages", adminPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/admins";
    }
}
