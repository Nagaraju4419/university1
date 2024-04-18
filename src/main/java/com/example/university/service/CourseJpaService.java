package com.example.university.service;

import com.example.university.model.*;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.ProfessorJpaRepository;
import com.example.university.repository.StudentJpaRepository;
import com.example.university.repository.CourseJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseJpaService implements CourseRepository {

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private ProfessorJpaRepository professorJpaRepository;

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Override
    public ArrayList<Course> getCourses() {
        List<Course> courseList = courseJpaRepository.findAll();
        ArrayList<Course> courses = new ArrayList<>(courseList);
        return courses;
    }

    @Override
    public Course getCourseById(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course addCourse(Course course) {
        List<Integer> studentIds = new ArrayList<>();

        for (Student student : course.getStudents()) {
            studentIds.add(student.getStudentId());
        }

        Professor professor = course.getProfessor();
        int professorId = professor.getProfessorId();

        try {
            List<Student> completeStundets = studentJpaRepository.findAllById(studentIds);
            if (studentIds.size() != completeStundets.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            course.setStudents(completeStundets);

            professor = professorJpaRepository.findById(professorId).get();
            course.setProfessor(professor);
            courseJpaRepository.save(course);
            return course;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Course updateCourse(int courseId, Course course) {
        try {
            Course newCourse = courseJpaRepository.findById(courseId).get();
            if (course.getCourseName() != null) {
                newCourse.setCourseName(course.getCourseName());
            }
            if (String.valueOf(course.getCredits()).length() > 0) {
                newCourse.setCredits(course.getCredits());
            }

            if (course.getProfessor() != null) {
                Professor professor = course.getProfessor();
                int professorId = professor.getProfessorId();
                Professor newProfessor = professorJpaRepository.findById(professorId).get();
                newCourse.setProfessor(newProfessor);
            }
            if ((course.getStudents()).size() > 0) {
                List<Student> students = course.getStudents();

                List<Student> newStudents = new ArrayList<>();
                for (Student student : students) {
                    int studentId = student.getStudentId();
                    Student newStudent = studentJpaRepository.findById(studentId).get();
                    newStudents.add(newStudent);
                }
                newCourse.setStudents(newStudents);
            }

            courseJpaRepository.save(newCourse);
            return newCourse;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }
    }

    // // Error from delete method
    // @Override
    // public void deleteCourse(int courseId) {
    // try {
    // courseJpaRepository.deleteById(courseId);
    // } catch (Exception e) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    // }
    // throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    // }

    @Override
    public void deleteCourse(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            List<Student> students = course.getStudents();
            for (Student student : students) {
                student.getCourses().remove(course);
            }
            studentJpaRepository.saveAll(students);
            courseJpaRepository.deleteById(courseId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "courseId " + courseId + " not found");
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public Professor getCoursesProfessor(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getProfessor();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public List<Student> getCoursesStudents(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getStudents();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}