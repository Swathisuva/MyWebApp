

package com.mycompany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MyWebAppApplication {

    public static void main(String[] args) {
        Logger logger= LoggerFactory.getLogger(MyWebAppApplication.class);
        logger.debug("Main application is Executed");
        SpringApplication.run(MyWebAppApplication.class, args);
    }

}
