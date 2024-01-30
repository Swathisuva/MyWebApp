
package com.mycompany.student.service;

import com.mycompany.student.entity.StudentDTO;
import com.mycompany.student.entity.Student;
import com.mycompany.student.repository.StudentRepository;
import com.mycompany.student.repository.UserInfoRepository;
import com.mycompany.student.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);


    private PasswordEncoder passwordEncoder;


    private UserInfoRepository repository;


    private StudentRepository repo;




    @Value("${spring.mail.username}")
    private String senderEmail;


StudentService(PasswordEncoder passwordEncoder,UserInfoRepository repository,StudentRepository repo){
    this.passwordEncoder=passwordEncoder;
    this.repository=repository;
    this.repo=repo;


}

    public List<StudentDTO> getAllStudents() {
        logger.debug("Fetching all students");
        List<Student> students = (List<Student>) repo.findAll();

        return students.stream().map(this::convertToDTO).toList();
    }

    public Optional<StudentDTO> getStudentById(Long id) {
        logger.debug("Finding student by id: {}", id);
        Optional<Student> studentOptional = repo.findById(Math.toIntExact(id));
        return studentOptional.map(this::convertToDTO);
    }

    public StudentDTO saveStudent(StudentDTO studentDTO) {
        // Convert StudentDTO to Student entity and save to the database
        Student student = StudentDTO.toEntity(studentDTO);
        repo.save(student);
        return studentDTO;
    }


    public void deleteStudent(Long id) {
        logger.debug("Deleting student by id: {}", id);
        repo.deleteById(Math.toIntExact(id));

    }

    public StudentDTO updateStudent(Long id, StudentDTO updatedStudentDTO) {
        logger.debug("Updating student with id: {}", id);

        Optional<Student> existingStudentOptional = repo.findById(Math.toIntExact(id));

        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();

            // Update the existing student with the new information
            existingStudent.setDept(updatedStudentDTO.getDept());
            existingStudent.setPassword(updatedStudentDTO.getPassword());
            existingStudent.setFirstName(updatedStudentDTO.getFirstName());
            existingStudent.setLastName(updatedStudentDTO.getLastName());

            Student updatedStudent = repo.save(existingStudent);

            return convertToDTO(updatedStudent);
        } else {
            logger.debug("Student with id {} not found", id);
            return null;
        }
    }

    // Helper methods for conversion between entity and DTO

    private StudentDTO convertToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setDept(student.getDept());
        studentDTO.setPassword(student.getPassword());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        return studentDTO;
    }

    private Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setDept(studentDTO.getDept());
        student.setPassword(studentDTO.getPassword());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        return student;
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "user added to system";
    }
    public StudentDTO addStudent(StudentDTO studentDTO) {
        // Convert StudentDTO to Student entity
        Student student = convertToEntity(studentDTO);

        // Save the student to the students table
        Student savedStudent = repo.save(student);

        return convertToDTO(savedStudent);
    }

}