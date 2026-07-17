package org.me.dibs.service;

import org.me.dibs.Repository.UserRepository;
import org.me.dibs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ImageService imageService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public void addUser(User user, MultipartFile profilePicture) throws IOException {
        if (profilePicture != null && !profilePicture.isEmpty()) {
            user.setProfilePicture(imageService.extractBytes(profilePicture));
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public void addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String username){
        return userRepo.findByUsername(username);
    }
}
