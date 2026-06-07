package org.me.dibs.service;

import org.me.dibs.Repository.userRepository;
import org.me.dibs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    userRepository userRepo;
    BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(10);
    public void addUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }
    public User getUser(String username){
        return userRepo.findByUsername(username);
    }
}
