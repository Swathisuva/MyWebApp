//package com.mycompany.student.email;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Scheduler {
//
//    @Scheduled(cron = "0 */20 * * * *")
//    public void sendEmailEveryTwentyMinutes() {
//        try {
//            String to = "swathi@email.com";
//            String subject = "Scheduled Email (Every 20 minutes)";
//            String text = "This is a scheduled email sent every 20 minutes.";
//            this.sendEmail(to, subject, text);
//        } catch (Exception e) {
//            handleEmailException(e);
//        }
//    }
//
//    @Scheduled(cron = "0 */5 * * * *")
//    public void sendEmailEveryFiveMinutes() {
//        try {
//            String to = "swathi@email.com";
//            String subject = "Scheduled Email (Every 5 minutes)";
//            String text = "This is a scheduled email sent every 5 minutes.";
//            this.sendEmail(to, subject, text);
//        } catch (Exception e) {
//            handleEmailException(e);
//        }
//    }
//}
//
//
