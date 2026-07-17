package org.me.dibs.service;

import org.me.dibs.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface UserService {
    void addUser(User user, MultipartFile profilePicture) throws IOException;
    void addUser(User user);
    User getUser(String username);
}
