package com.mycompany.student.Repository;



import com.mycompany.student.Entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student,Integer> {
    List<Student> findByfirstName(String firstName);
}
