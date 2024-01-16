
package com.mycompany.user.Controller;

import com.mycompany.user.Entity.StudentDTO;
import com.mycompany.user.Service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService service;

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        logger.debug("List of all students");
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        logger.debug("Getting student by id: {}", id);
        Optional<StudentDTO> studentOptional = service.getStudentById(id);
        return studentOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDTO> saveStudent(@RequestBody StudentDTO studentDTO) {
        logger.debug("Saving student: {}", studentDTO);
        StudentDTO savedStudent = service.saveStudent(studentDTO);
        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO updatedStudentDTO) {
        logger.debug("Updating student with id: {}", id);

        StudentDTO updatedStudent = service.updateStudent(id, updatedStudentDTO);

        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        } else {
            logger.debug("Student with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        logger.debug("Deleting user by id: {}", id);
        service.deleteStudent(id);
    }
}
