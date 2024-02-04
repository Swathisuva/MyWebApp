package com.mycompany.student.controller;




import com.mycompany.student.security.AuthRequest;
import com.mycompany.student.security.JWTService;
import com.mycompany.student.service.UserService;
import com.mycompany.student.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;


    @Autowired
    public UserController(AuthenticationManager authenticationManager, JWTService jwtService,  UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/auth/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return userService.addUser(userInfo);
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            // Perform authentication without disclosing details about valid or invalid usernames
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // If authentication is successful, generate and return the token
            String token = jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            // Authentication failed, return a generic error message
            throw new BadCredentialsException("Invalid credentials");
        }
    }

}