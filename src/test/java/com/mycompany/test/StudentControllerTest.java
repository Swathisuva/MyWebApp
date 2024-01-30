//////package com.mycompany;
//////
//////import com.fasterxml.jackson.databind.ObjectMapper;
//////import com.mycompany.user.Entity.Student;
//////import com.mycompany.user.Repository.StudentRepository;
//////import com.mycompany.user.Controller.StudentController;
//////import com.mycompany.user.Service.StudentService;
//////import org.junit.jupiter.api.Test;
//////import org.mockito.Mockito;
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//////import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//////import org.springframework.boot.test.context.SpringBootTest;
//////import org.springframework.boot.test.mock.mockito.MockBean;
//////import org.springframework.http.MediaType;
//////import org.springframework.test.web.servlet.MockMvc;
//////import static org.hamcrest.Matchers.hasSize;
//////
//////import java.util.Collections;
//////import java.util.Optional;
//////
//////import static org.mockito.ArgumentMatchers.any;
//////import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//////import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//////@SpringBootTest
//////@WebMvcTest(StudentController.class)
//////@AutoConfigureMockMvc
//////public class StudentRepositoryTests {
//////
//////    @Autowired
//////    private MockMvc mockMvc;
//////
//////    @Autowired
//////    private ObjectMapper objectMapper;
//////
//////    @MockBean
//////    private StudentRepository studentRepository;
//////
//////    @Autowired
//////    private StudentService studentservice;
//////    @Test
//////    public void testAddNew() throws Exception {
//////        Student student = new Student();
//////        student.setPassword("swathi072003*");
//////        student.setFirstName("Swathiii");
//////        student.setLastName("S");
//////
//////        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(student);
//////
//////        mockMvc.perform(post("/students")
//////                        .contentType(MediaType.APPLICATION_JSON)
//////                        .content(objectMapper.writeValueAsString(student)))
//////                .andExpect(status().isOk())
//////                .andExpect(jsonPath("$.id").exists());
//////    }
//////
//////    @Test
//////    public void testListAll() throws Exception {
//////        Mockito.when(studentRepository.findAll()).thenReturn(Collections.singletonList(new Student()));
//////
//////        mockMvc.perform(get("/students"))
//////                .andExpect(status().isOk())
//////                .andExpect(jsonPath("$", hasSize(1)));
//////    }
//////
//////    @Test
//////    public void testUpdate() throws Exception {
//////        Integer userId = 1;
//////        Student student = new Student();
//////        student.setId(userId);
//////        student.setPassword("1234");
//////
//////        Mockito.when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
//////        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(student);
//////
//////        mockMvc.perform(put("/students/{id}", userId)
//////                        .contentType(MediaType.APPLICATION_JSON)
//////                        .content(objectMapper.writeValueAsString(student)))
//////                .andExpect(status().isOk())
//////                .andExpect(jsonPath("$.password").value("1234"));
//////    }
//////
//////    @Test
//////    public void testGet() throws Exception {
//////        Integer userId = 2;
//////        Student student = new Student();
//////        student.setId(userId);
//////
//////        Mockito.when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
//////
//////        mockMvc.perform(get("/students/{id}", userId))
//////                .andExpect(status().isOk())
//////                .andExpect(jsonPath("$.id").value(userId));
//////    }
//////
//////    @Test
//////    public void testDelete() throws Exception {
//////        Integer userId = 2;
//////
//////        mockMvc.perform(delete("/students/{id}", userId))
//////                .andExpect(status().isOk());
//////
//////        Mockito.verify(studentRepository, Mockito.times(1)).deleteById(userId);
//////    }
//////}
////
////package com.example.mycompany;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.mycompany.student.Controller.StudentController;
////import com.mycompany.student.Entity.StudentDTO;
////import com.mycompany.student.Service.StudentService;
////import org.junit.jupiter.api.Test;
////import org.junit.jupiter.api.extension.ExtendWith;
////import org.junit.runner.RunWith;
////import org.mockito.InjectMocks;
////import org.mockito.Mock;
////import org.mockito.MockitoAnnotations;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.http.MediaType;
////import org.springframework.test.context.junit.jupiter.SpringExtension;
////import org.springframework.test.context.junit4.SpringRunner;
////import org.springframework.test.web.servlet.MockMvc;
////import org.springframework.test.web.servlet.setup.MockMvcBuilders;
////
////import java.util.Arrays;
////import java.util.Optional;
////
////import static org.mockito.Mockito.*;
////import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
////import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
////@ExtendWith(SpringExtension.class)
////@SpringBootTest
////@AutoConfigureMockMvc
////public class StudentControllerTests {
////
////    @Mock
////    private StudentService studentService;
////
////    @InjectMocks
////    private StudentController studentController;
////
////    @Autowired
////    private MockMvc mockMvc;
////
////    private final ObjectMapper objectMapper = new ObjectMapper();
////
////    public StudentControllerTests() {
////        MockitoAnnotations.openMocks(this);
////        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
////    }
////
////    @Test
////    void testGetAllStudents() throws Exception {
////        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
////                new StudentDTO(1L, "IT", "swathi", "swatttt", "123sedd"),
////                new StudentDTO(2L, "cse", "wereee", "hjgkhjgk", "isgfisyf")
////        ));
////
////        mockMvc.perform(get("/students"))
////                .andExpect(status().isOk())
////                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
////                .andExpect(jsonPath("$[0].id").value(1L))
////                .andExpect(jsonPath("$[0].dept").value("IT"))
////                .andExpect(jsonPath("$[0].firstName").value("swathi"))
////                .andExpect(jsonPath("$[0].lastName").value("swatttt"))
////                .andExpect(jsonPath("$[0].password").value("123sedd"))
////                .andExpect(jsonPath("$[1].id").value(2L))
////                .andExpect(jsonPath("$[1].dept").value("cse"))
////                .andExpect(jsonPath("$[1].firstName").value("wereee"))
////                .andExpect(jsonPath("$[1].lastName").value("hjgkhjgk"))
////                .andExpect(jsonPath("$[1].password").value("isgfisyf"));
////
////        verify(studentService, times(1)).getAllStudents();
////        verifyNoMoreInteractions(studentService);
////    }
////
////    @Test
////    void testGetStudentById() throws Exception {
////        Long studentId = 1L;
////        StudentDTO studentDTO = new StudentDTO(studentId, "IT", "password123", "John", "Doe");
////
////        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(studentDTO));
////
////        mockMvc.perform(get("/students/{id}", studentId))
////                .andExpect(status().isOk())
////                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
////                .andExpect(jsonPath("$.id").value(studentId))
////                .andExpect(jsonPath("$.dept").value("IT"))
////                .andExpect(jsonPath("$.password").value("password123"))
////                .andExpect(jsonPath("$.firstName").value("John"))
////                .andExpect(jsonPath("$.lastName").value("Doe"));
////
////        verify(studentService, times(1)).getStudentById(studentId);
////        verifyNoMoreInteractions(studentService);
////    }
////
////    @Test
////    void testUpdateStudent() throws Exception {
////        Long id = 10L;
////
////        StudentDTO updatedStudentDTO = new StudentDTO();
////        updatedStudentDTO.setId(id);
////        updatedStudentDTO.setDept("Updated Department");
////        updatedStudentDTO.setPassword("UpdatedPassword");
////        updatedStudentDTO.setFirstName("Updated FirstName");
////        updatedStudentDTO.setLastName("Updated LastName");
////
////        mockMvc.perform(put("/students/{id}", id)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(updatedStudentDTO)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.dept").value("Updated Department"))
////                .andExpect(jsonPath("$.password").value("UpdatedPassword"))
////                .andExpect(jsonPath("$.firstName").value("Updated FirstName"))
////                .andExpect(jsonPath("$.lastName").value("Updated LastName"));
////    }
////
////    @Test
////    void testSaveStudent() throws Exception {
////        StudentDTO newStudentDTO = new StudentDTO();
////        newStudentDTO.setDept("New Department");
////        newStudentDTO.setPassword("New Password");
////        newStudentDTO.setFirstName("New FirstName");
////        newStudentDTO.setLastName("New LastName");
////
////        mockMvc.perform(post("/students")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(newStudentDTO)))
////                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.id").doesNotExist())
////                .andExpect(jsonPath("$.dept").value("New Department"))
////                .andExpect(jsonPath("$.password").value("New Password"))
////                .andExpect(jsonPath("$.firstName").value("New FirstName"))
////                .andExpect(jsonPath("$.lastName").value("New LastName"));
////    }
////
////    @Test
////    void testDeleteStudent() throws Exception {
////        Long studentIdToDelete = 10L;
////
////        mockMvc.perform(delete("/students/{id}", studentIdToDelete))
////                .andExpect(status().isOk());
////    }
////}
////package com.mycompany;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.mycompany.student.Controller.StudentController;
////import com.mycompany.student.Entity.StudentDTO;
////import com.mycompany.student.Service.StudentService;
////import com.mycompany.student.security.AuthRequest;
////import com.mycompany.student.security.JWTService;
////import org.junit.jupiter.api.Test;
////
////import org.mockito.InjectMocks;
////
////import org.springframework.beans.factory.annotation.Autowired;
////
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.boot.test.mock.mockito.MockBean;
////
////import org.springframework.http.MediaType;
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.test.web.servlet.MockMvc;
////import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
////import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
////import org.springframework.mail.MailSender;
////import org.springframework.jms.core.JmsTemplate;
////
////
////import java.util.Arrays;
////import java.util.Optional;
////
////import static org.mockito.ArgumentMatchers.any;
////import static org.mockito.Mockito.*;
////
//////@WebMvcTest(StudentController.class)
//////@ExtendWith(MockitoExtension.class)
////@SpringBootTest
////class StudentControllerTest {
////
////    @InjectMocks
////    private StudentController studentController;
////
////    @Autowired
////    private MockMvc mockMvc;
////
////    @MockBean(name = "passwordEncoder")
////    private PasswordEncoder passwordEncoder;
////
////    @MockBean(name = "authenticationManager")
////    private AuthenticationManager authenticationManager;
////
////    @MockBean(name = "studentService")
////    private StudentService studentService;
////
////    @MockBean(name = "jwtService")
////    private JWTService jwtService;
////
////    @MockBean(name = "userDetailsService")
////    private UserDetailsService userDetailsService;
////
////    @MockBean(name = "mailSender")
////    private MailSender mailSender;
////
////    @MockBean(name = "jmsTemplate")
////    private JmsTemplate jmsTemplate;
////    @Test
////    void getAllStudents_ReturnsListOfStudents() throws Exception {
////        when(studentService.getAllStudents()).thenReturn(Arrays.asList(new StudentDTO(), new StudentDTO()));
////
////        mockMvc.perform(MockMvcRequestBuilders.get("/students")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
////    }
////
////    @Test
////    void authenticateAndGetToken_ValidCredentials_ReturnsToken() throws Exception {
////        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
////                .thenReturn(new UsernamePasswordAuthenticationToken("username", "password"));
////        when(jwtService.generateToken("username")).thenReturn("mockedToken");
////
////        mockMvc.perform(MockMvcRequestBuilders.post("/students/authenticate")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(asJsonString(new AuthRequest("username", "password"))))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.content().string("mockedToken"));
////    }
////
////    private String asJsonString(Object obj) throws Exception {
////        return new ObjectMapper().writeValueAsString(obj);
////    }
////
////    @Test
////    void getStudentById_ExistingStudent_ReturnsStudent() throws Exception {
////        Long studentId = 1L;
////        StudentDTO studentDTO = new StudentDTO();
////        studentDTO.setId(studentId);
////
////        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(studentDTO));
////
////        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", studentId)
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(studentId));
////    }
////
////    @Test
////    void getStudentById_NonExistingStudent_ReturnsNotFound() throws Exception {
////        Long nonExistingId = 999L;
////        when(studentService.getStudentById(nonExistingId)).thenReturn(Optional.empty());
////
////        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", nonExistingId)
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isNotFound());
////    }
////
////    @Test
////    void saveStudent_ValidStudent_ReturnsSavedStudent() throws Exception {
////        StudentDTO studentDTO = new StudentDTO();
////        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(studentDTO);
////
////        mockMvc.perform(MockMvcRequestBuilders.post("/students")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(asJsonString(studentDTO)))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
////    }
////
////    @Test
////    void saveStudent_InvalidStudent_ReturnsBadRequest() throws Exception {
////        StudentDTO invalidStudent = new StudentDTO(); // Provide invalid data
////
////        mockMvc.perform(MockMvcRequestBuilders.post("/students")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(asJsonString(invalidStudent)))
////                .andExpect(MockMvcResultMatchers.status().isBadRequest());
////    }
////}
//package com.mycompany.test;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mycompany.student.controller.StudentController;
//import com.mycompany.student.entity.StudentDTO;
//import com.mycompany.student.security.AuthRequest;
//import com.mycompany.student.service.StudentService;
//import com.mycompany.student.security.JWTService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Collections;
//
//@WebMvcTest(StudentController.class)
//public class StudentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private StudentService studentService;
//
//    @MockBean
//    private JWTService jwtService;
//
//    @Test
//    @WithMockUser(authorities = "ROLE_USER")
//    public void testGetAllStudents() throws Exception {
//        Mockito.when(studentService.getAllStudents()).thenReturn(Collections.singletonList(new StudentDTO()));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/students")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    public void testAuthenticateAndGetToken() throws Exception {
//        AuthRequest authRequest = new AuthRequest("username", "password");
//        Mockito.when(jwtService.generateToken(Mockito.anyString())).thenReturn("mockedToken");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/students/authenticate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("mockedToken"));
//    }
//
//    // Add more test cases for other methods as needed
//}
