//
//// StudentController.java
//package com.mycompany.user.Controller;
//
//import com.mycompany.user.AuthRequest;
//import com.mycompany.user.Entity.StudentDTO;
//
//import com.mycompany.user.Service.StudentService;
//import com.mycompany.user.JWTService;
//import com.mycompany.user.UserInfo;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//@RestController
//@RequestMapping("/students")
//@SecurityRequirement(name="Bearer-key")
//public class StudentController {
//
//    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private StudentService service;
//    @Autowired
//    private JWTService jwtService;
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<List<StudentDTO>> getAllStudents() {
//        logger.debug("List of all students");
//        List<StudentDTO> students = service.getAllStudents();
//        return ResponseEntity.ok(students);
//    }
//    @PostMapping("/authenticate")
//    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(authRequest.getUsername());
//        } else {
//            throw new UsernameNotFoundException("invalid user request !");
//        }
//
//
//    }
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
//        logger.debug("Getting student by id: {}", id);
//        Optional<StudentDTO> studentOptional = service.getStudentById(id);
//        return studentOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<StudentDTO> saveStudent(@RequestBody StudentDTO studentDTO) {
//        logger.debug("Saving student: {}", studentDTO);
//        StudentDTO savedStudent = service.saveStudent(studentDTO);
//        return ResponseEntity.ok(savedStudent);
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO updatedStudentDTO) {
//        logger.debug("Updating student with id: {}", id);
//
//        StudentDTO updatedStudent = service.updateStudent(id, updatedStudentDTO);
//
//        if (updatedStudent != null) {
//            return ResponseEntity.ok(updatedStudent);
//        } else {
//            logger.debug("Student with id {} not found", id);
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @PostMapping("/new")
//
//    public String addNewUser(@RequestBody UserInfo userInfo) {
//
//        return service.addUser(userInfo);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
//        logger.debug("Deleting student by id: {}", id);
//        service.deleteStudent(id);
//        return ResponseEntity.ok().build();
//    }
//}
package com.mycompany.user.Controller;

import com.mycompany.user.AuthRequest;
import com.mycompany.user.Entity.StudentDTO;
import com.mycompany.user.Service.StudentService;
import com.mycompany.user.JWTService;
import com.mycompany.user.UserInfo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/students")
@SecurityRequirement(name="Bearer-key")

public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StudentService service;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;


    private String applicationName;



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


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.debug("Deleting student by id: {} from {}", id, applicationName);
        service.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
