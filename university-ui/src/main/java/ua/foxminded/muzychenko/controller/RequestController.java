package ua.foxminded.muzychenko.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.muzychenko.controller.validator.ParamValidator;
import ua.foxminded.muzychenko.dto.UserRequestDTO;
import ua.foxminded.muzychenko.dto.profile.ShortUserInfo;
import ua.foxminded.muzychenko.enums.RequestTypeEnum;
import ua.foxminded.muzychenko.service.UserRequestService;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final UserRequestService userRequestService;
    private final ParamValidator paramValidator;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") String page,
                        @RequestParam(defaultValue = "5") String size,
                        Model model) {

        Map<String, Integer> pageRequest = paramValidator.getValidatedPageRequest(page, size);

        int pageCount = pageRequest.get("page");
        int pageSize = pageRequest.get("size");

        Page<UserRequestDTO> requestDTOSPage = userRequestService.findAll(pageCount, pageSize);

        model.addAttribute("requests", requestDTOSPage.getContent());
        model.addAttribute("currentPage", requestDTOSPage.getNumber() + 1);
        model.addAttribute("totalItems", requestDTOSPage.getTotalElements());
        model.addAttribute("totalPages", requestDTOSPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "request/requests";
    }

    @GetMapping("/{requestId}")
    public String requestInfo(@PathVariable String requestId, Model model) {
       // userRequestService.find
    }

    @PostMapping("/")
    public String createRequest(@ModelAttribute UserRequestDTO userRequestDTO) {
        ShortUserInfo shortUserInfo = new ShortUserInfo(userRequestDTO.getId(), userRequestDTO.getUserRole());
        RequestTypeEnum requestTypeEnum = userRequestDTO.getRequestTypeDTO().getType();
        userRequestService.createRequest(requestTypeEnum, shortUserInfo);

        return null;
    }

    @PostMapping("/approve{requestId}")
    public String approveRequest(@PathVariable String requestId) {
        userRequestService.approveRequest(UUID.fromString(requestId));

        return null;
    }

    @PostMapping("/reject{requestId}")
    public String rejectRequest(@PathVariable String requestId) {
        userRequestService.rejectRequest(UUID.fromString(requestId));

        return null;
    }
}
