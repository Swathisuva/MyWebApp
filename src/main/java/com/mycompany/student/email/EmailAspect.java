package com.mycompany.student.email;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;

@Aspect
@Component
public class EmailAspect {

    @Autowired
    private MailSender mailSender;

    // Define pointcut expressions for the methods you want to intercept
    @Before("execution(* com.mycompany.student.Controller.StudentController.*(..)) && !execution(* com.mycompany.student.Controller.StudentController.authenticateAndGetToken(..))")
    public void sendEmailBeforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String to = "swathi@email.com";
        String subject = "Method Execution Started: " + methodName;
        String text = "The method " + methodName + " has started its execution.";
        sendEmail(to, subject, text);
    }

    @After("execution(* com.mycompany.student.Controller.StudentController.*(..)) && !execution(* com.mycompany.student.Controller.StudentController.authenticateAndGetToken(..))")
    public void sendEmailAfterMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String to = "swathi@email.com";
        String subject = "Method Execution Completed: " + methodName;
        String text = "The method " + methodName + " has completed its execution.";
        sendEmail(to, subject, text);
    }
    // Scheduler method for 1-Minute Intervals
    @Scheduled(cron = "0 */20 * * * *")
    public void sendEmailEveryTwoSeconds() {
        String to = "swathi@email.com";
        String subject = "Scheduled Email (Every 20 minutes)";
        String text = "This is a scheduled email sent every 1 minute.";

        sendEmail(to, subject, text);
    }


    // Scheduler method for 1-Hour Intervals
    @Scheduled(cron = "0  */5 * * * *")
    public void sendEmailEveryHour() {
        String to = "swathi@email.com";
        String subject = "Scheduled Email (Every 5min)";
        String text = "This is a scheduled email sent every 5 minutes.";
        sendEmail(to, subject, text);
    }

    // Email sending logic
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
