package com.example.university.repository;

import com.example.university.model.*;

import java.util.ArrayList;
import java.util.List;

public interface CourseRepository {
    ArrayList<Course> getCourses();

    Course getCourseById(int courseId);

    Course addCourse(Course course);

    Course updateCourse(int courseId, Course course);

    void deleteCourse(int courseId);

    Professor getCoursesProfessor(int courseId);

    List<Student> getCoursesStudents(int courseId);

}
