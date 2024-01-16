package com.mycompany.user.Service;
import com.mycompany.user.Repository.StudentRepository;
import com.mycompany.user.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private StudentRepository repo;


    public List<Student> getAllStudents() {
        logger.debug("Showing all up the users");
        return (List<Student>) repo.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        logger.debug("Finding all te user");
        return repo.findById(Math.toIntExact(id));
    }

    public Student saveStudent(Student student) {
        logger.debug("Saving the book");
        return repo.save(student);
    }

    public void deleteStudent(Long id) {
        logger.debug("Deleting up the users");
        repo.deleteById(Math.toIntExact(id));
    }
    public Student updateStudent(Long id, Student updatedStudent) {
        logger.debug("Updating student with id: {}", id);

        Optional<Student> existingStudentOptional = repo.findById(id.intValue());

        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();

            // Update the existing student with the new information
            existingStudent.setDept(updatedStudent.getDept());
            existingStudent.setPassword(updatedStudent.getPassword());
//            existingStudent.setFirstName(updatedStudent.getFirstName());
//            existingStudent.setLastName(updatedStudent.getLastName());
            // Add more fields to update as needed

            // Save the updated student
            return repo.save(existingStudent);
        } else {
            logger.debug("Student with id {} not found", id);
            // You might want to throw an exception or handle the error in a different way
            return null;
        }
    }
}
//
//    public void save(User user) {
//        logger.info("user saved succesfully");
//        repo.save(user);
//    }
//    public User get(Integer id) throws UserNotFoundException {
//        Optional<User> result=repo.findById(id);
//        if (result.isPresent()){
//            return result.get();
//        }
//        throw new UserNotFoundException("Could not find any user with id" + id);
//    }
//
//    public void delete(Integer id) throws UserNotFoundException {
//        Long count=repo.countById(id);
//        if (count==null || count==0){
//            throw new UserNotFoundException("Could not find any user with id" + id);
//        }
//        repo.deleteById(id);
//    }
//
//
//}
