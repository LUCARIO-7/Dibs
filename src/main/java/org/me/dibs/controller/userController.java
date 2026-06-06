package org.me.dibs.controller;

import org.me.dibs.model.User;
import org.me.dibs.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    @Autowired
    userService UserService;
    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestBody  User user){
        System.out.println("password is"+user.getPassword());
       
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
