package com.mycompany.user.Controller;

import com.mycompany.user.Service.StudentService;
import com.mycompany.user.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService service;

    @GetMapping
    public List<Student> getAllStudents() {
        logger.debug("List of all students");
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudentById(@PathVariable Long id) {
        logger.debug("Getting all the Students By id");
        return service.getStudentById(id);
    }

    @PostMapping
    public Student saveStudent(@RequestBody Student student) {
        logger.debug("Showing all the Users");
        return service.saveStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        logger.debug("Updating student with id: {}", id);

        return service.updateStudent(id,updatedStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        logger.debug("Deleting up the users by id");
        service.deleteStudent(id);
    }
}
