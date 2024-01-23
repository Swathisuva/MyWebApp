//package com.mycompany.student.email;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
////import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.MailSender;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//
//public class Email {
//    @Autowired
//    private MailSender mailSender;
//    @RequestMapping("/sendMail")
//    public String sendConfirmationEmail() {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo("swathi@email.com");
//        msg.setSubject("message testing");
//        msg.setText("Hello World from Spring Boot Email");
//        mailSender.send(msg);
//        return "OK";
//    }
//}