
package com.mycompany.student.email;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;


import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Aspect
@Component
public class EmailAspect {


    private MailSender mailSender;

    public EmailAspect(MailSender mailSender){
        this.mailSender=mailSender;
    }

    @After("execution(* com.mycompany.student.controller.StudentController.*(..)) && !execution(* com.mycompany.student.controller.StudentController.authenticateAndGetToken(..))")
    public void sendEmailAfterMethodExecution(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String to = "swathi@jmail.com";
            String subject = "Method Execution Completed: " + methodName;
            String text = "The method " + methodName + " has completed its execution.";
            this.sendEmail(to, subject, text);
        } catch (Exception e) {
            handleEmailException(e);
        }
    }

    @AfterThrowing(pointcut = "execution(* com.mycompany.student.controller.StudentController.*(..)) && !execution(* com.mycompany.student.controller.StudentController.authenticateAndGetToken(..))", throwing = "e")
    public void sendEmailOnFailure(JoinPoint joinPoint, Exception e) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String to = "swathi@jmail.com";
            String subject = "Method Execution Failed: " + methodName;
            String text = "The method " + methodName + " has failed with the following exception:\n" + e.getMessage();
            this.sendEmail(to, subject, text);
        } catch (Exception ex) {
            handleEmailException(ex);
        }
    }

    @Scheduled(cron = "0 */20 * * * *")
    public void sendEmailEveryTwentyMinutes() {
        try {
            String to = "swathi@email.com";
            String subject = "Scheduled Email (Every 20 minutes)";
            String text = "This is a scheduled email sent every 20 minutes.";
            this.sendEmail(to, subject, text);
        } catch (Exception e) {
            handleEmailException(e);
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void sendEmailEveryFiveMinutes() {
        try {
            String to = "swathi@email.com";
            String subject = "Scheduled Email (Every 5 minutes)";
            String text = "This is a scheduled email sent every 5 minutes.";
            this.sendEmail(to, subject, text);
        } catch (Exception e) {
            handleEmailException(e);
        }
    }
    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            this.mailSender.send(message);
        } catch (Exception e) {
            handleEmailException(e);
        }
    }

    private void handleEmailException(Exception e) {

        LOGGER.error("An error occurred while handling email: {}", e.getMessage(), e);


    }
}

