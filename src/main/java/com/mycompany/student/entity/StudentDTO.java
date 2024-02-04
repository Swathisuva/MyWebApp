//studentDTO.java
package com.mycompany.student.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable {

    private Long id;
    private String dept;
    private String password;
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    private String lastName;

    // No need for explicit constructors, getters, and setters with Lombok annotations.


public static StudentDTO fromEntity(Student student) {
    return new StudentDTO(
            student.getId(),
            student.getDept(),
            student.getPassword(),
            student.getFirstName(),
            student.getLastName()
    );
}
public static Student toEntity(StudentDTO studentDTO) {
    Student student = new Student();
    student.setId(studentDTO.getId());
    student.setDept(studentDTO.getDept());
    student.setPassword(studentDTO.getPassword());
    student.setFirstName(studentDTO.getFirstName());
    student.setLastName(studentDTO.getLastName());
    return student;
}
}

