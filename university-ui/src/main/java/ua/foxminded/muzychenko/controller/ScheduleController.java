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
import ua.foxminded.muzychenko.service.LessonTimeService;
import ua.foxminded.muzychenko.service.ScheduleService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;
    private GroupService groupService;
    private CourseService courseService;
    private LessonTimeService lessonTimeService;

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

    @GetMapping("/group/{group-name}")
    public String getScheduleForGroup(@PathVariable("group-name") String groupName, Model model) {
        Map<WeekNumber, Map<DayOfWeek, List<LessonInfo>>> scheduleMap = scheduleService.getScheduleForGroup(groupName)
            .getTwoWeeksSchedule();

        model.addAttribute("weekNumbers", WeekNumber.values());
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        model.addAttribute("lessonTimes", lessonTimeService.lessonTimes());
        model.addAttribute("firstWeekLessons", scheduleMap.get(WeekNumber.FIRST));
        model.addAttribute("secondWeekLessons", scheduleMap.get(WeekNumber.SECOND));

        return "schedule/view_schedule";
    }

}
