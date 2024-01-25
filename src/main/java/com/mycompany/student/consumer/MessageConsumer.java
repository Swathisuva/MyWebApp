//package com.mycompany.student.consumer;
//
//
//import com.mycompany.student.Entity.StudentDTO;
//import com.mycompany.student.queue.SystemMessage;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @author AJ Catambay of Bridging Code
// *
// */
//@Component
//public class MessageConsumer {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
//
//    @JmsListener(destination = "bridgingcode-queue")
//    public void messageListener(SystemMessage systemMessage) {
//        LOGGER.info("Message received! {}", systemMessage);
////        service.saveStudent();
//    }
//}

package com.mycompany.student.consumer;


import com.mycompany.student.Entity.StudentDTO;
import com.mycompany.student.Service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author AJ Catambay of Bridging Code
 *
 */
@Component
public class MessageConsumer {

    @Autowired
    private StudentService studentService; // Inject your service

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "bridgingcode-queue")
    public void messageListener(StudentDTO studentDTO) {
        LOGGER.info("Message received! {}", studentDTO);

        // Use a service class to save the student data to the database
        studentService.saveStudent(studentDTO);
    }
}