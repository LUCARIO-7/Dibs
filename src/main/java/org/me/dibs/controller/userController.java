package org.me.dibs.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.me.dibs.model.Item;
import org.me.dibs.model.User;
import org.me.dibs.service.ItemService;
import org.me.dibs.service.JwtService;
import org.me.dibs.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class userController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    userService UserService;
    @Autowired
    ItemService itemService;
    @Autowired
    JwtService jwtService;
    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestBody  User user){
        System.out.println("password is"+user.getPassword());
        UserService.addUser(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response){
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );

        String token=jwtService.generateToken(user.getUsername());
        Cookie cookie= new Cookie("jwtoken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24*60*60);
        response.addCookie(cookie);
        return  "login successfull";
    }
    @GetMapping("/user")
    ResponseEntity<String> getCurrentUser(Authentication authentication){
        if(authentication!=null && authentication.isAuthenticated()){
            return new ResponseEntity<>(authentication.getName(),HttpStatus.OK);
        }
        return new ResponseEntity<>("not autheticated",HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/claimedItems")
    ResponseEntity<List<Item>> getClaimedItems(Principal principal){
        List<Item> claimedItems= itemService.getClaimedItems(principal.getName());
        return new ResponseEntity<>(claimedItems,HttpStatus.OK);
    }
}
