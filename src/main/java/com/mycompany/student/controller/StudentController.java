
package com.mycompany.student.controller;


import com.mycompany.student.entity.StudentDTO;
import com.mycompany.student.service.StudentService;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/students")
@SecurityRequirement(name="Bearer-key")

public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);







    private StudentService service;









    private String applicationName;


    private JmsTemplate jmsTemplate;



    StudentController(JmsTemplate jmsTemplate,StudentService service){
        this.jmsTemplate = jmsTemplate;


        this.service=service;


    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        logger.debug("List of all students from {}", applicationName);
        List<StudentDTO> students = service.getAllStudents();
        return ResponseEntity.ok(students);
    }



//@Cacheable(value="students")
@GetMapping("/{id}")
@PreAuthorize("hasAuthority('ROLE_USER')")
public ResponseEntity<?> getStudentById(@PathVariable Long id) {
    logger.debug("Getting student by id: {} from {}", id, applicationName);

    try {
        StudentDTO student = service.getStudentById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return ResponseEntity.ok(student);
    } catch (IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());

        errorResponse.put("Error", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}




//    @CacheEvict(value="students")
@PostMapping
@PreAuthorize("hasAuthority('ROLE_USER')")
public ResponseEntity<?> saveStudent(@RequestBody @Valid StudentDTO studentDTO, BindingResult bindingResult) {
    logger.debug("Saving student: {} from {}", studentDTO, applicationName);

    if (bindingResult.hasErrors()) {
        // Validation errors occurred
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", "Invalid input");
        errorResponse.put("details", getValidationErrors(bindingResult));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    try {
        // Additional validation checks
        if (studentDTO.getPassword().length() >= 10) {
            throw new IllegalArgumentException("Password length should be less than 10 characters");
        }



        StudentDTO savedStudent = service.saveStudent(studentDTO);
        return ResponseEntity.ok(savedStudent);
    } catch (IllegalArgumentException ex) {
        // Handle additional validation errors
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

    private Map<String, String> getValidationErrors(BindingResult bindingResult) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return validationErrors;
    }






    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO updatedStudentDTO) {
        logger.debug("Updating student with id: {} from {}", id, applicationName);

        StudentDTO updatedStudent = service.updateStudent(id, updatedStudentDTO);

        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        } else {
            logger.debug("Student with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/addstudent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentDTO studentDTO) {
        logger.debug("Adding student: {} from {}", studentDTO, applicationName);
        StudentDTO savedStudent = service.addStudent(studentDTO);
        return ResponseEntity.ok(savedStudent);
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
//        logger.debug("Deleting student by id: {} from {}", id, applicationName);
//        service.deleteStudent(id);
//        return ResponseEntity.ok().build();
//    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.debug("Deleting student by id: {} from {}", id, applicationName);
        try {
            service.deleteStudent(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.debug("Student with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }


    //    @PostMapping("/publishMessage")
//    public ResponseEntity<String> publishMessage(@RequestBody StudentDTO studentDTO) {
//        try {
//            jmsTemplate.convertAndSend("student_queue", studentDTO);
//            return new ResponseEntity<>("Sent.", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping("/publishMessage")
    public ResponseEntity<?> publishMessage(@RequestBody StudentDTO studentDTO) {
        try {
            jmsTemplate.convertAndSend("student_queue", studentDTO);
            return new ResponseEntity<>("Sent.", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error publishing message:", e);
            return new ResponseEntity<>("Failed to publish message.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
