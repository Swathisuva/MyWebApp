////package com.mycompany;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.mycompany.user.Entity.Student;
////import com.mycompany.user.Repository.StudentRepository;
////import com.mycompany.user.Controller.StudentController;
////import com.mycompany.user.Service.StudentService;
////import org.junit.jupiter.api.Test;
////import org.mockito.Mockito;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
////import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.boot.test.mock.mockito.MockBean;
////import org.springframework.http.MediaType;
////import org.springframework.test.web.servlet.MockMvc;
////import static org.hamcrest.Matchers.hasSize;
////
////import java.util.Collections;
////import java.util.Optional;
////
////import static org.mockito.ArgumentMatchers.any;
////import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
////import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
////@SpringBootTest
////@WebMvcTest(StudentController.class)
////@AutoConfigureMockMvc
////public class StudentRepositoryTests {
////
////    @Autowired
////    private MockMvc mockMvc;
////
////    @Autowired
////    private ObjectMapper objectMapper;
////
////    @MockBean
////    private StudentRepository studentRepository;
////
////    @Autowired
////    private StudentService studentservice;
////    @Test
////    public void testAddNew() throws Exception {
////        Student student = new Student();
////        student.setPassword("swathi072003*");
////        student.setFirstName("Swathiii");
////        student.setLastName("S");
////
////        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(student);
////
////        mockMvc.perform(post("/students")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(student)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id").exists());
////    }
////
////    @Test
////    public void testListAll() throws Exception {
////        Mockito.when(studentRepository.findAll()).thenReturn(Collections.singletonList(new Student()));
////
////        mockMvc.perform(get("/students"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$", hasSize(1)));
////    }
////
////    @Test
////    public void testUpdate() throws Exception {
////        Integer userId = 1;
////        Student student = new Student();
////        student.setId(userId);
////        student.setPassword("1234");
////
////        Mockito.when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
////        Mockito.when(studentRepository.save(any(Student.class))).thenReturn(student);
////
////        mockMvc.perform(put("/students/{id}", userId)
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(student)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.password").value("1234"));
////    }
////
////    @Test
////    public void testGet() throws Exception {
////        Integer userId = 2;
////        Student student = new Student();
////        student.setId(userId);
////
////        Mockito.when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
////
////        mockMvc.perform(get("/students/{id}", userId))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.id").value(userId));
////    }
////
////    @Test
////    public void testDelete() throws Exception {
////        Integer userId = 2;
////
////        mockMvc.perform(delete("/students/{id}", userId))
////                .andExpect(status().isOk());
////
////        Mockito.verify(studentRepository, Mockito.times(1)).deleteById(userId);
////    }
////}
//
//package com.mycompany;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mycompany.student.Controller.StudentController;
//import com.mycompany.student.Controller.UserController;
//import com.mycompany.student.Entity.StudentDTO;
//import com.mycompany.student.Repository.StudentRepository;
//import com.mycompany.student.Service.StudentService;
//import com.mycompany.student.Service.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class StudentControllerTests {
//
//    @Mock
//    private UserService uservice;
//
//    @InjectMocks
//    private StudentController studentController;
//    @InjectMocks
//    private UserController userController;
//    @Autowired
//    private  MockMvc mockMvc;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    public StudentControllerTests() {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
//    }
//
//
//    @Autowired
//    private StudentRepository repository;
//
//    @Autowired
//    private StudentService studentService;
//
//
//    @Test
//    void testGetAllStudents() throws Exception {
//        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
//                new StudentDTO(1L, "IT", "swathi", "swatttt", "123sedd"),
//                new StudentDTO(2L, "cse", "wereee", "hjgkhjgk", "isgfisyf")
//        ));
//
//        mockMvc.perform(get("/students"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].dept").value("IT"))
//                .andExpect(jsonPath("$[0].firstName").value("swathi"))
//                .andExpect(jsonPath("$[0].lastName").value("swatttt"))
//                .andExpect(jsonPath("$[0].password").value("123sedd"))
//                .andExpect(jsonPath("$[1].id").value(2L))
//                .andExpect(jsonPath("$[1].dept").value("cse"))
//                .andExpect(jsonPath("$[1].firstName").value("wereee"))
//                .andExpect(jsonPath("$[1].lastName").value("hjgkhjgk"))
//                .andExpect(jsonPath("$[1].password").value("isgfisyf"));
//
//        verify(studentService, times(1)).getAllStudents();
//        verifyNoMoreInteractions(studentService);
//    }
//
//
//    @Test
//    void testGetStudentById() throws Exception {
//        Long studentId = 1L;
//        StudentDTO studentDTO = new StudentDTO(studentDTO, "IT", "Information Technology");
//
//        when(studentService.getDepartmentDTOById(departmentId)).thenReturn(Optional.of(departmentDTO));
//
//        mockMvc.perform(get("/department/{id}", departmentId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("IT"))
//                .andExpect(jsonPath("$.description").value("Information Technology"));
//
//        verify(departmentService, times(1)).getDepartmentDTOById(departmentId);
//        verifyNoMoreInteractions(departmentService);
//    }
//
//
//    @Test
//    void testUpdateStudent() throws Exception {
//        Long id = 10L;
//
//        StudentDTO updatedStudentDTO = new StudentDTO();
//        updatedStudentDTO.setId(id);
//        updatedStudentDTO.setDept("Updated Department");
//        updatedStudentDTO.setPassword("UpdatedPassword");
//        updatedStudentDTO.setFirstName("Updated FirstName");
//        updatedStudentDTO.setLastName("Updated LastName");
//
//
//        mockMvc.perform(put("/students/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedStudentDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.dept").value("Updated Department"))
//                .andExpect(jsonPath("$.password").value("UpdatedPassword"))
//                .andExpect(jsonPath("$.firstName").value("Updated FirstName"))
//                .andExpect(jsonPath("$.lastName").value("Updated LastName"));
//                 // Check for boolean value, not a string
//    }
//    @Test
//    void testSaveStudent() throws Exception {
//        StudentDTO newStudentDTO = new StudentDTO();
//        newStudentDTO.setId(null);
//        newStudentDTO.setDept("New Department");
//        newStudentDTO.setPassword("New Password");
//        newStudentDTO.setFirstName("New FirstName");
//        newStudentDTO.setLastName("New LastName");
//
//        mockMvc.perform(post("/students")  // Assuming the endpoint for saving students is "/students"
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newStudentDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").doesNotExist())  // Assuming you don't want to expose ID in the response
//                .andExpect(jsonPath("$.dept").value("New Department"))
//                .andExpect(jsonPath("$.password").value("New Password"))
//                .andExpect(jsonPath("$.firstName").value("New FirstName"))
//                .andExpect(jsonPath("$.lastName").value("New LastName"));
//    }
//
//
//
//
//    @Test
//    void testDeleteStudent() throws Exception {
//        // Assuming you want to delete the student with ID 10
//        Long studentIdToDelete = 10L;
//
//        mockMvc.perform(delete("/students/{id}", studentIdToDelete))
//                .andExpect(status().isOk());  // Update to expect a 200 OK status
//    }
//
//}
