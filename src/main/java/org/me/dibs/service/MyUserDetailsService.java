package org.me.dibs.service;

import jakarta.transaction.Transactional;
import org.me.dibs.Repository.userRepository;
import org.me.dibs.model.User;
import org.me.dibs.controller.userPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    userRepository userRepo;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepo.findByUsername(username);
        if(user==null)
            throw  new UsernameNotFoundException("user not found");
        return new userPrincipal(user);
    }
}
