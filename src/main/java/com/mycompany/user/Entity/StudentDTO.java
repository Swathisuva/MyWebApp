//studentDTO.java
package com.mycompany.user.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long id;
    private String dept;
    private String password;
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

