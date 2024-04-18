package com.example.university.service;

import com.example.university.model.*;
import com.example.university.repository.StudentRepository;
import com.example.university.repository.ProfessorJpaRepository;
import com.example.university.repository.StudentJpaRepository;
import com.example.university.repository.CourseJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class StudentJpaService implements StudentRepository {

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Override
    public ArrayList<Student> getStudents() {
        List<Student> studentList = studentJpaRepository.findAll();
        ArrayList<Student> students = new ArrayList<>(studentList);
        return students;
    }

    @Override
    public Student getStudentById(int studentId) {
        try {
            Student student = studentJpaRepository.findById(studentId).get();
            return student;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // Null values adding
    @Override
    public Student addStudent(Student student) {
        try {
            studentJpaRepository.save(student);
            return student;
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Student updateStudent(int studentId, Student student) {
        try {
            Student newStudent = studentJpaRepository.findById(studentId).get();
            if (student.getStudentName() != null) {
                newStudent.setStudentName(student.getStudentName());
            }
            if (student.getEmail() != null) {
                newStudent.setEmail(student.getEmail());
            }
            if (student.getCourses().size() > 0) {
                newStudent.setCourses(student.getCourses());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            return studentJpaRepository.save(newStudent);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // @Override
    // public void deleteStudent(int studentId) {
    // try {
    // studentJpaRepository.deleteById(studentId);
    // } catch (Exception e) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    // }
    // throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    // }

    @Override
    public void deleteStudent(int studentId) {
        try {
            Student student = studentJpaRepository.findById(studentId).get();
            List<Course> courses = student.getCourses();
            for (Course course : courses) {
                course.getStudents().remove(student);
            }
            courseJpaRepository.saveAll(courses);
            studentJpaRepository.deleteById(studentId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "studentId " + studentId + " not found");
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Course> getStudentCourses(int studentId) {
        try {
            Student student = studentJpaRepository.findById(studentId).get();
            return student.getCourses();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}