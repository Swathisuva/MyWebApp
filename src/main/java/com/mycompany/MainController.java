package com.mycompany;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public class MainController {

    @GetMapping("")
    public String showHomePage(){
        System.out.println("main contrroler");
        return "index";
    }
}
