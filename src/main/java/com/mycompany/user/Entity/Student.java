
//student.java
package com.mycompany.user.Entity;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Table(name="students")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=45)
    private String dept;

    @Column(length=15, nullable=false)
    private String password;

    @Column(length=45, nullable=false, name="first_name")
    private String firstName;

    @Column(length=45, nullable=false, name="last_name")
    private String lastName;
}
