package com.mycompany.student.repository;



import com.mycompany.student.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student,Integer> {
    List<Student> findByfirstName(String firstName);
}
