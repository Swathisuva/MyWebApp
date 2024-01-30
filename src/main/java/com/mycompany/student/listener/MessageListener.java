

package com.mycompany.student.listener;


import com.mycompany.student.entity.StudentDTO;
import com.mycompany.student.service.StudentService;

import jakarta.jms.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author AJ Catambay of Bridging Code
 *
 */
@Component
public class MessageListener {


    private StudentService studentService; // Inject your service

    MessageListener(StudentService studentService){
        this.studentService=studentService;
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "student_queue")
    public void messageListener(StudentDTO studentDTO) {
        LOGGER.info("Message received! {}", studentDTO);

        // Use a service class to save the student data to the database
        studentService.saveStudent(studentDTO);
    }
}