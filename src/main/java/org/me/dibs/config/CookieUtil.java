package org.me.dibs.config;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwtoken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge((long)24 * 60 * 60)
                .build();
    }
}
