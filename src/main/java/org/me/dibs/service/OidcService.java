package org.me.dibs.service;

import org.me.dibs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OidcService extends OidcUserService {
    @Autowired
    userService UserService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
       OidcUser oidcUser=super.loadUser(userRequest);
       String email=oidcUser.getAttribute("email");
       User user=UserService.getUser(email);
       System.out.println(email);
       if(user==null){
           User user1=new User();
           user1.setUsername(email);
           user1.setPassword("");
           UserService.addUser(user1);
       }
        return oidcUser;
    }
}
