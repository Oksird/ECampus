# University Class Timetable Application

Welcome to the University Class Timetable Application! This application helps students, teachers, and administrators manage and access class schedules within the university structure.

## Table of Contents

- [User Roles](#user-roles)
- [Admin User Stories](#admin-user-stories)
- [Student User Stories](#student-user-stories)
- [Teacher User Stories](#teacher-user-stories)


## User Roles

1. **Admin:**
   - Admins have the highest level of access and control over the application.
   - Admins can create, edit, and delete schedules for both students and teachers.
   
2. **Student:**
   - Students can view schedules for their own group on a weekly or monthly basis.
   - Students can also search for teachers' schedules.
   
3. **Teacher:**
   - Teachers can view schedules for all groups, as well as schedules for all teachers.

## Admin User Stories

### Admin can create a schedule:

*Given the user is logged in as an Admin,*

- The user can navigate to the `Admin Dashboard`.
- The user can access the `Create Schedule` option.
- The user should be able to create schedules for both students and teachers.

### Admin can edit a schedule:

*Given the user is logged in as an Admin,*

- The user can navigate to the `Admin Dashboard`.
- The user can access the `Edit Schedule` option.
- The user should be able to edit existing schedules for both students and teachers.

### Admin can delete a schedule:

*Given the user is logged in as an Admin,*

- The user can navigate to the `Admin Dashboard`.
- The user can access the `Delete Schedule` option.
- The user should be able to delete schedules for both students and teachers.

## Student User Stories

### Student can view their group's weekly schedule:

*Given the user is logged in as a Student,*

- The user can access the `View Schedule` option.
- The user should see the weekly schedule for their own group.

### Student can view their group's monthly schedule:

*Given the user is logged in as a Student,*

- The user can access the `View Schedule` option.
- The user can select a specific month.
- The user should see the monthly schedule for their own group.

### Student can search for a teacher's schedule:

*Given the user is logged in as a Student,*

- The user can access the `Search Teacher Schedule` option.
- The user can enter the teacher's name.
- The user should see the schedule for the specified teacher.

## Teacher User Stories

### Teacher can view schedules for all groups:

*Given the user is logged in as a Teacher,*

- The user can access the `View Schedule` option.
- The user should see schedules for all groups, including their own.

### Teacher can view schedules for all teachers:

*Given the user is logged in as a Teacher,*

- The user can access the `View Schedule` option.
- The user should see schedules for all teachers, including themselves.

## UML Diagram

Here is a UML diagram illustrating the application structure:

![UML Diagram](img/Class_UML.svg)