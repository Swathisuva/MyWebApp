package com.mycompany.user.Repository;



import com.mycompany.user.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student,Integer> {
    List<Student> findByfirstName(String firstName);
}
