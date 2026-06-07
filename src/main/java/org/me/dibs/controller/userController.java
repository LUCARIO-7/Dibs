package org.me.dibs.controller;

import org.me.dibs.model.User;
import org.me.dibs.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    userService UserService;
    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestBody  User user){
        System.out.println("password is"+user.getPassword());
        UserService.addUser(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
        if(authentication.isAuthenticated())
            return "authenticated";
        else
            return  "not authenticated";
    }
}
