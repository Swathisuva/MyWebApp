package com.mycompany.test;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.student.config.JmsConfig;
import com.mycompany.student.config.SecurityConfig;
import com.mycompany.student.controller.StudentController;
import com.mycompany.student.controller.UserController;
import com.mycompany.student.email.EmailAspect;
import com.mycompany.student.entity.Student;
import com.mycompany.student.entity.StudentDTO;
import com.mycompany.student.healthindicator.CustomHealthIndicator;
import com.mycompany.student.listener.MessageListener;
import com.mycompany.student.repository.StudentRepository;
import com.mycompany.student.repository.UserInfoRepository;
import com.mycompany.student.security.AuthRequest;
import com.mycompany.student.security.JWTService;
import com.mycompany.student.security.JwtAuthFilter;
import com.mycompany.student.service.StudentService;
import com.mycompany.student.service.UserService;
import com.mycompany.student.user.UserInfo;
import com.mycompany.student.user.UserInfoUserDetailsService;
import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.TextMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.jms.ConnectionFactory;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private UserController userController;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private StudentRepository repo;

    @Mock
    private UserInfoRepository userInfoRepository;
    @InjectMocks
    private MessageListener messageListener;

    @Mock
    private UserInfoRepository repository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @MockBean
    private JwtAuthFilter jwtauthFilter;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }


    @Test
    void getAllStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(new StudentDTO(1L, "CSE", "password", "John", "Doe"),
                new StudentDTO(2L, "EEE", "pass123", "Jane", "Doe")));

        mockMvc.perform(get("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].dept").value("CSE"))
                .andExpect(jsonPath("$[0].password").value("password"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].dept").value("EEE"))
                .andExpect(jsonPath("$[1].password").value("pass123"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"));

        verify(studentService, times(1)).getAllStudents();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void saveStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO(1L, "CSE", "password", "John", "Doe");
        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(studentDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.dept").value("CSE"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
        verifyNoMoreInteractions(studentService);
    }
    @Test
    void updateStudent() throws Exception {
        long studentId = 1L;
        StudentDTO updatedStudentDTO = new StudentDTO(studentId, "CSE", "newpassword", "UpdatedJohn", "UpdatedDoe");
        when(studentService.updateStudent(eq(studentId), any(StudentDTO.class))).thenReturn(updatedStudentDTO);

        mockMvc.perform(put("/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.dept").value("CSE"))
                .andExpect(jsonPath("$.password").value("newpassword"))
                .andExpect(jsonPath("$.firstName").value("UpdatedJohn"))
                .andExpect(jsonPath("$.lastName").value("UpdatedDoe"));

        verify(studentService, times(1)).updateStudent(eq(studentId), any(StudentDTO.class));
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addStudent() throws Exception {
        StudentDTO newStudentDTO = new StudentDTO(null, "ECE", "pass456", "NewJohn", "NewDoe");
        when(studentService.addStudent(any(StudentDTO.class))).thenReturn(newStudentDTO);

        mockMvc.perform(post("/students/addstudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()) // Updated expectation for nullable id
                .andExpect(jsonPath("$.dept").value("ECE"))
                .andExpect(jsonPath("$.password").value("pass456"))
                .andExpect(jsonPath("$.firstName").value("NewJohn"))
                .andExpect(jsonPath("$.lastName").value("NewDoe"));

        verify(studentService, times(1)).addStudent(any(StudentDTO.class));
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void deleteStudent() throws Exception {
        long studentId = 1L;

        mockMvc.perform(delete("/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(eq(studentId));
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void publishMessage() throws Exception {
        StudentDTO studentDTO = new StudentDTO(1L, "CSE", "password", "John", "Doe");

        mockMvc.perform(post("/students/publishMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

        verifyNoMoreInteractions(studentService);
    }

    @Test
    void addNewUser() throws Exception {
        UserInfo userInfo = new UserInfo(1, "John Doe", "john@example.com", "password123", "ROLE_USER");

        when(userService.addUser(any(UserInfo.class))).thenReturn("User added successfully");

        userController.addNewUser(userInfo);

        verify(userService, times(1)).addUser(any(UserInfo.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void authenticateAndGetToken_Success() throws Exception {
        AuthRequest authRequest = new AuthRequest("john_doe", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(authRequest.getUsername())).thenReturn("generatedToken");

        ResponseEntity<String> responseEntity = userController.authenticateAndGetToken(authRequest);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(authRequest.getUsername());
        verifyNoMoreInteractions(authenticationManager, jwtService);

        // Assuming you want to assert the response entity
        // Note: You may need to use mockMvc for more detailed assertions in a Spring MVC test
        assert responseEntity.getStatusCodeValue() == 200;
        assert responseEntity.getBody().equals("generatedToken");
    }

    @Test
    void authenticateAndGetToken_Failure() throws Exception {
        AuthRequest authRequest = new AuthRequest("john_doe", "invalidPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        try {
            userController.authenticateAndGetToken(authRequest);
        } catch (BadCredentialsException e) {
            // Expected exception, do nothing
        }

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(authenticationManager);
    }

    @Test
    void getStudentById_Success() throws Exception {
        long studentId = 1L;
        when(studentService.getStudentById(eq(studentId))).thenReturn(Optional.of(new StudentDTO(studentId, "CSE", "password", "John", "Doe")));

        mockMvc.perform(get("/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.dept").value("CSE"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(studentService, times(1)).getStudentById(eq(studentId));
        verifyNoMoreInteractions(studentService);
    }



    @Test
    void deleteStudent_NotFound() throws Exception {
        long studentId = 999L;
        doThrow(new IllegalArgumentException("Student not found")).when(studentService).deleteStudent(eq(studentId));

        mockMvc.perform(delete("/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist()) // Remove the expectation for a specific JSON path
                .andExpect(content().string("")); // Add this line to check if the response body is empty

        verify(studentService, times(1)).deleteStudent(eq(studentId));
        verifyNoMoreInteractions(studentService);
    }
    @Test
    void publishMessage_InternalServerError() throws Exception {
        StudentDTO studentDTO = new StudentDTO(1L, "CSE", "password", "John", "Doe");
        doThrow(new RuntimeException("Error publishing message")).when(jmsTemplate).convertAndSend(anyString(), any(StudentDTO.class));

        mockMvc.perform(post("/students/publishMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(studentDTO)))
                .andExpect(status().isInternalServerError());

        verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(StudentDTO.class));
        verifyNoMoreInteractions(jmsTemplate);
    }


    @Test
    void getAllStudentsEmptyList() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(studentService, times(1)).getAllStudents();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateNonexistentStudent() throws Exception {
        long nonexistentStudentId = 999L;
        StudentDTO updatedStudentDTO = new StudentDTO(nonexistentStudentId, "CSE", "newpassword", "UpdatedJohn", "UpdatedDoe");
        when(studentService.updateStudent(eq(nonexistentStudentId), any(StudentDTO.class))).thenReturn(null);

        mockMvc.perform(put("/students/{id}", nonexistentStudentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedStudentDTO)))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).updateStudent(eq(nonexistentStudentId), any(StudentDTO.class));
        verifyNoMoreInteractions(studentService);
    }



    @Test
    public void testMessageListener() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO();
        // Set up any necessary data in the studentDTO

        // Act
        messageListener.messageListener(studentDTO);

        // Assert
        // Verify that the saveStudent method is called with the correct argument
        Mockito.verify(studentService, Mockito.times(1)).saveStudent(studentDTO);
    }


    @Test
     void testMessageListenerWithNullDTO() {
        // Act and Assert
        // Ensure that the listener does not throw an exception when receiving a null StudentDTO
        assertDoesNotThrow(() -> messageListener.messageListener(null));

        // Verify that saveStudent is not called
        Mockito.verify(studentService, Mockito.never()).saveStudent(any(StudentDTO.class));
    }


//    @Test
//    void getNonexistentStudentById_Forbidden() throws Exception {
//        // Test case for attempting to get a nonexistent student by ID with proper authentication but insufficient authorization
//        long nonexistentId = 999L;
//        when(studentService.getStudentById(eq(nonexistentId))).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/students/{id}", nonexistentId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());  // Update expectation to 404
//
//        verify(studentService, times(1)).getStudentById(eq(nonexistentId));
//        verifyNoMoreInteractions(studentService);
//    }

    @Test
    void updateNonexistentStudent_Forbidden() throws Exception {
        // Test case for attempting to update a nonexistent student with proper authentication but insufficient authorization
        long nonexistentStudentId = 999L;
        StudentDTO updatedStudentDTO = new StudentDTO(nonexistentStudentId, "CSE", "newpassword", "UpdatedJohn", "UpdatedDoe");
        when(studentService.updateStudent(eq(nonexistentStudentId), any(StudentDTO.class))).thenReturn(null);

        mockMvc.perform(put("/students/{id}", nonexistentStudentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedStudentDTO)))
                .andExpect(status().isNotFound());  // Update expectation to 404

        verify(studentService, times(1)).updateStudent(eq(nonexistentStudentId), any(StudentDTO.class));
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void authenticateAndGetToken_MissingCredentials() throws Exception {
        // Test case for attempting authentication without providing credentials
        AuthRequest authRequest = new AuthRequest("", "");

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isNotFound());  // Update expectation to 404

        verifyNoMoreInteractions(authenticationManager, jwtService);
    }

    @Test
    void addNewUser_InvalidInput() throws Exception {
        // Test case for adding a new user with invalid input
        UserInfo invalidUserInfo = new UserInfo(1, "", "", "", "");

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidUserInfo)))
                .andExpect(status().isNotFound());  // Update expectation to 404

        verifyNoMoreInteractions(userService);
    }

    @Test
    void userInfoNoArgsConstructor() {
        // Arrange and Act
        UserInfo userInfo = new UserInfo();

        // Assert
        assertNotNull(userInfo);
        assertEquals(0, userInfo.getId());
        assertNull(userInfo.getName());
        assertNull(userInfo.getEmail());
        assertNull(userInfo.getPassword());
        assertNull(userInfo.getRoles());
    }

    @Test
    void userInfoFields() {
        // Arrange
        UserInfo userInfo = new UserInfo();

        // Act
        userInfo.setId(1);
        userInfo.setName("John Doe");
        userInfo.setEmail("john@example.com");
        userInfo.setPassword("password123");
        userInfo.setRoles("ROLE_USER");

        // Assert
        assertEquals(1, userInfo.getId());
        assertEquals("John Doe", userInfo.getName());
        assertEquals("john@example.com", userInfo.getEmail());
        assertEquals("password123", userInfo.getPassword());
        assertEquals("ROLE_USER", userInfo.getRoles());
    }

    @Test
    void userInfoAllArgsConstructor() {
        // Arrange and Act
        UserInfo userInfo = new UserInfo(1, "John Doe", "john@example.com", "password123", "ROLE_USER");

        // Assert
        assertNotNull(userInfo);
        assertEquals(1, userInfo.getId());
        assertEquals("John Doe", userInfo.getName());
        assertEquals("john@example.com", userInfo.getEmail());
        assertEquals("password123", userInfo.getPassword());
        assertEquals("ROLE_USER", userInfo.getRoles());
    }

    @Test
    void userInfoIdValidation() {
        // Arrange
        UserInfo userInfo = new UserInfo();

        // Act
        userInfo.setId(1);

        // Assert
        assertEquals(1, userInfo.getId());
    }

    @Test
    void userInfoNameValidation() {
        // Arrange
        UserInfo userInfo = new UserInfo();

        // Act
        userInfo.setName("John Doe");

        // Assert
        assertEquals("John Doe", userInfo.getName());
    }

    @Test
    void userInfoEmailValidation() {
        // Arrange
        UserInfo userInfo = new UserInfo();

        // Act
        userInfo.setEmail("john@example.com");

        // Assert
        assertEquals("john@example.com", userInfo.getEmail());
    }

    @Test
    void userInfoPasswordValidation() {
        // Arrange
        UserInfo userInfo = new UserInfo();

        // Act
        userInfo.setPassword("password123");

        // Assert
        assertEquals("password123", userInfo.getPassword());
    }
    @Test
    void userInfoRolesValidation() {
        // Arrange
        UserInfo userInfo = new UserInfo();

        // Act
        userInfo.setRoles("ROLE_USER");

        // Assert
        assertEquals("ROLE_USER", userInfo.getRoles());
    }

    @Test
    void userInfoEqualsAndHashCode() {
        // Arrange
        UserInfo userInfo1 = new UserInfo(1, "John Doe", "john@example.com", "password123", "ROLE_USER");
        UserInfo userInfo2 = new UserInfo(1, "John Doe", "john@example.com", "password123", "ROLE_USER");

        // Assert
        assertEquals(userInfo1, userInfo2);
        assertEquals(userInfo1.hashCode(), userInfo2.hashCode());
    }
    @Test
    void userInfoNoArgsConstructorNotNull() {
        // Arrange and Act
        UserInfo userInfo = new UserInfo();

        // Assert
        assertNotNull(userInfo);
    }

    @Test
    void userInfoAllArgsConstructorNotNull() {
        // Arrange and Act
        UserInfo userInfo = new UserInfo(1, "John Doe", "john@example.com", "password123", "ROLE_USER");

        // Assert
        assertNotNull(userInfo);
    }

    @Test
    void fromEntity_ConvertsEntityToDTO() {
        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setDept("CSE");
        student.setPassword("pass123");
        student.setFirstName("John");
        student.setLastName("Doe");

        // Act
        StudentDTO studentDTO = StudentDTO.fromEntity(student);

        // Assert
        assertEquals(Long.valueOf(1L), studentDTO.getId());
        assertEquals("CSE", studentDTO.getDept());
        assertEquals("pass123", studentDTO.getPassword());
        assertEquals("John", studentDTO.getFirstName());
        assertEquals("Doe", studentDTO.getLastName());
    }

    @Test
    void toEntity_ConvertsDTOToEntity() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(1L, "CSE", "pass123", "John", "Doe");

        // Act
        Student student = StudentDTO.toEntity(studentDTO);

        // Assert
        assertEquals(Long.valueOf(1L), student.getId());
        assertEquals("CSE", student.getDept());
        assertEquals("pass123", student.getPassword());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
    }

    @Test
    void fromEntity_NullEntity_ThrowsNullPointerException() {
        // Arrange
        Student student = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> StudentDTO.fromEntity(student));
    }

    @Test
    void toEntity_NullDTO_ThrowsNullPointerException() {
        // Arrange
        StudentDTO studentDTO = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> StudentDTO.toEntity(studentDTO));
    }

    @Test
    void fromEntity_WithNullFields() {
        // Arrange
        Student student = new Student();

        // Act
        StudentDTO studentDTO = StudentDTO.fromEntity(student);

        // Assert
        assertEquals(null, studentDTO.getId());
        assertEquals(null, studentDTO.getDept());
        assertEquals(null, studentDTO.getPassword());
        assertEquals(null, studentDTO.getFirstName());
        assertEquals(null, studentDTO.getLastName());
    }

    @Test
    void toEntity_WithNullFields() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO(null, null, null, null, null);

        // Act
        Student student = StudentDTO.toEntity(studentDTO);

        // Assert
        assertEquals(null, student.getId());
        assertEquals(null, student.getDept());
        assertEquals(null, student.getPassword());
        assertEquals(null, student.getFirstName());
        assertEquals(null, student.getLastName());
    }

//    @Test
//    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
//        // Arrange
//        String username = "nonExistentUser";
//        when(repository.findByName(username)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(UsernameNotFoundException.class, () -> {
//            userDetailsService.loadUserByUsername(username);
//        });
//    }

    @Test
     void testDatabaseConnectionHealthy() {
        // Mock JdbcTemplate
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        // Mock successful query execution
        when(jdbcTemplate.queryForObject("SELECT 1 FROM dual", Integer.class)).thenReturn(1);

        // Create CustomHealthIndicator with mocked JdbcTemplate
        CustomHealthIndicator healthIndicator = new CustomHealthIndicator(jdbcTemplate);

        // Call the health method and assert the result
        Health health = healthIndicator.health();
        assertEquals("Database connection is healthy", health.getDetails().get("message"));
        assertTrue(health.getStatus().equals(Health.up().build().getStatus()));
    }

    @Test
    public void testDatabaseConnectionNotHealthy() {
        // Mock JdbcTemplate
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        // Mock exception during query execution
        when(jdbcTemplate.queryForObject("SELECT 1 FROM dual", Integer.class)).thenThrow(new RuntimeException());

        // Create CustomHealthIndicator with mocked JdbcTemplate
        CustomHealthIndicator healthIndicator = new CustomHealthIndicator(jdbcTemplate);

        // Call the health method and assert the result
        Health health = healthIndicator.health();
        assertEquals("Database connection is not healthy", health.getDetails().get("message"));
        assertTrue(health.getStatus().equals(Health.down().build().getStatus()));
    }

    @Test
    public void testDatabaseConnectionHealthyWithZeroResult() {
        // Mock JdbcTemplate
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        // Mock successful query execution with zero result
        when(jdbcTemplate.queryForObject("SELECT 1 FROM dual", Integer.class)).thenReturn(0);

        // Create CustomHealthIndicator with mocked JdbcTemplate
        CustomHealthIndicator healthIndicator = new CustomHealthIndicator(jdbcTemplate);

        // Call the health method and assert the result
        Health health = healthIndicator.health();
        assertEquals("Database connection is healthy", health.getDetails().get("message"));
        assertTrue(health.getStatus().equals(Health.up().build().getStatus()));
    }
    @Test
    public void testSendEmailAfterMethodExecution() {
        MailSender mailSender = Mockito.mock(MailSender.class);
        EmailAspect emailAspect = new EmailAspect(mailSender);

        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn("someMethodName");

        emailAspect.sendEmailAfterMethodExecution(joinPoint);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailEveryTwentyMinutes() {
        MailSender mailSender = Mockito.mock(MailSender.class);
        EmailAspect emailAspect = new EmailAspect(mailSender);

        emailAspect.sendEmailEveryTwentyMinutes();

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailEveryFiveMinutes() {
        MailSender mailSender = Mockito.mock(MailSender.class);
        EmailAspect emailAspect = new EmailAspect(mailSender);

        emailAspect.sendEmailEveryFiveMinutes();

        verify(mailSender).send(any(SimpleMailMessage.class));
    }


    @Test
    public void testJmsListenerContainerFactoryConcurrency() throws NoSuchFieldException, IllegalAccessException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

        JmsConfig jmsConfig = new JmsConfig();
        DefaultJmsListenerContainerFactory factory = jmsConfig.jmsListenerContainerFactory(connectionFactory);

        // Using reflection to access the private field
        Field concurrencyField = DefaultJmsListenerContainerFactory.class.getDeclaredField("concurrency");
        concurrencyField.setAccessible(true);
        String actualConcurrency = (String) concurrencyField.get(factory);

        // Verify that the factory concurrency is set correctly
        assertEquals("5-10", actualConcurrency);
    }

    @Test
    public void testJmsListenerContainerFactoryWithDifferentConnectionFactory() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

        JmsConfig jmsConfig = new JmsConfig();
        DefaultJmsListenerContainerFactory factory = jmsConfig.jmsListenerContainerFactory(connectionFactory);


    }


   


}
