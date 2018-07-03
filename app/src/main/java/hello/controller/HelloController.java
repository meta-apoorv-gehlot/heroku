package hello.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.service.UserService;

@RestController
public class HelloController {
    
    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index() {
        return "Spring Boot Demo Application";
    }
}
