package ua.foxminded.muzychenko.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.LessonCreationDto;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.LessonNumber;
import ua.foxminded.muzychenko.enums.TypeOfLesson;
import ua.foxminded.muzychenko.enums.WeekNumber;
import ua.foxminded.muzychenko.service.CourseService;
import ua.foxminded.muzychenko.service.GroupService;
import ua.foxminded.muzychenko.service.ScheduleService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;
    private GroupService groupService;
    private CourseService courseService;

    @GetMapping("/new")
    public String getCreateLessonForm(Model model) {

        List<String> groupNames = groupService.findAll()
            .stream()
            .map(GroupInfo::getGroupName)
            .toList();

        List<String> courseNames = courseService.findAll()
            .stream()
            .map(CourseInfo::getCourseName)
            .toList();

        model.addAttribute("types", TypeOfLesson.values());
        model.addAttribute("lessonNumbers", LessonNumber.values());
        model.addAttribute("weekNumbers", WeekNumber.values());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("groups", groupNames);
        model.addAttribute("courses", courseNames);
        model.addAttribute("lesson", new LessonCreationDto());

        return "schedule/create_lesson";
    }

    @PostMapping("/")
    public String createLesson(@ModelAttribute LessonCreationDto lessonCreationDto) {
        scheduleService.createLesson(lessonCreationDto);
        return "redirect:/schedule/new";
    }

    @GetMapping("/group/{group-id}/{week-number}")
    public String getScheduleForGroup(@PathVariable("group-id") String groupId,
                                      @PathVariable("week-number") String weekNumber,
                                      Model model) {

        Map<WeekNumber, Map<DayOfWeek, List<LessonInfo>>> scheduleMap = scheduleService.getScheduleForGroup(
            groupService.findGroupById(UUID.fromString(groupId))
                .getGroupName()
            )
            .getTwoWeeksSchedule();

        model.addAttribute("weekNumber", weekNumber);
        model.addAttribute("days", DayOfWeek.values());
        model.addAttribute("lessonNumbers", LessonNumber.values());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("currentGroupId", groupId);
        model.addAttribute("currentGroupName", groupService.findGroupById(UUID.fromString(groupId)).getGroupName());

        if (weekNumber.equals("1")) {
            model.addAttribute("weekSchedule", scheduleMap.get(WeekNumber.FIRST));
        }
        if (weekNumber.equals("2")) {
            model.addAttribute("weekSchedule", scheduleMap.get(WeekNumber.SECOND));
        }

        return "schedule/view_schedule";
    }
}
