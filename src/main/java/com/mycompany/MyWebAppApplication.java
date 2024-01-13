//package com.mycompany;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//
//
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//
//public class MyWebAppApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(MyWebAppApplication.class, args);
//    }
//}

package com.mycompany;

import com.mycompany.user.userController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mycompany.user")
public class MyWebAppApplication{
    Logger logger = LoggerFactory.getLogger(userController.class);

    public static void main(String[] args) {
        SpringApplication.run(MyWebAppApplication.class, args);
    }
}
