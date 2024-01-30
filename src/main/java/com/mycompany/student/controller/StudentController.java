
package com.mycompany.student.controller;

import com.mycompany.student.security.AuthRequest;
import com.mycompany.student.entity.StudentDTO;
import com.mycompany.student.service.StudentService;
import com.mycompany.student.security.JWTService;
import com.mycompany.student.user.UserInfo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/students")
@SecurityRequirement(name="Bearer-key")

public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);




    private AuthenticationManager authenticationManager;


    private StudentService service;


    private JWTService jwtService;






    private String applicationName;


    private JmsTemplate jmsTemplate;



    StudentController(JmsTemplate jmsTemplate,JWTService jwtService,StudentService service,AuthenticationManager authenticationManager){
        this.jmsTemplate = jmsTemplate;

        this.jwtService=jwtService;
        this.service=service;
        this.authenticationManager=authenticationManager;

    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        logger.debug("List of all students from {}", applicationName);
        List<StudentDTO> students = service.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        logger.debug("Getting student by id: {} from {}", id, applicationName);
        Optional<StudentDTO> studentOptional = service.getStudentById(id);
        return studentOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StudentDTO> saveStudent(@RequestBody StudentDTO studentDTO) {
        logger.debug("Saving student: {} from {}", studentDTO, applicationName);
        StudentDTO savedStudent = service.saveStudent(studentDTO);


        return ResponseEntity.ok(savedStudent);
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

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }
    @PostMapping("/addstudent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentDTO studentDTO) {
        logger.debug("Adding student: {} from {}", studentDTO, applicationName);
        StudentDTO savedStudent = service.addStudent(studentDTO);
        return ResponseEntity.ok(savedStudent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.debug("Deleting student by id: {} from {}", id, applicationName);
        service.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/publishMessage")
    public ResponseEntity<String> publishMessage(@RequestBody StudentDTO studentDTO) {
        try {
            jmsTemplate.convertAndSend("student_queue", studentDTO);
            return new ResponseEntity<>("Sent.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
