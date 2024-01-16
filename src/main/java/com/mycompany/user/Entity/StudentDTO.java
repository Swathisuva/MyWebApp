package com.mycompany.user.Entity;

public class StudentDTO {

    private Integer id;
    private String dept;
    private String password;
    private String firstName;
    private String lastName;
    private String enabled;

    // Constructors, getters, and setters...

    public StudentDTO() {
        // Default constructor
    }

    public StudentDTO(Integer id, String dept, String password, String firstName, String lastName, String enabled) {
        this.id = id;
        this.dept = dept;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
