package org.me.dibs.service;

import org.springframework.security.core.userdetails.UserDetails;
import java.security.Key;

public interface JwtService {
    String generateToken(String username);
    Key getKey();
    String extractUserName(String token);
    boolean validateToken(String token, UserDetails userDetails);
}
