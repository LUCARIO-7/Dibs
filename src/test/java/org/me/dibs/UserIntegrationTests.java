package org.me.dibs;

import org.junit.jupiter.api.Test;
import org.me.dibs.Repository.UserDetailRepository;
import org.me.dibs.Repository.userRepository;
import org.me.dibs.model.User;
import org.me.dibs.model.UserDetail;
import org.me.dibs.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTests {

    @Autowired
    private userService userService;

    @Autowired
    private userRepository userRepo;

    @Autowired
    private UserDetailRepository userDetailRepo;

    @Test
    public void contextLoads() {
        assertNotNull(userService);
        assertNotNull(userRepo);
        assertNotNull(userDetailRepo);
    }

    @Test
    public void testUserRegistrationWithDetails() {
        User user = new User();
        user.setUsername("integration_user");
        user.setPassword("raw_password");

        UserDetail detail = new UserDetail();
        detail.setFirstName("John");
        detail.setLastName("Doe");
        detail.setEmail("john@example.com");
        detail.setUser(user);
        user.setUserDetail(detail);

        // Register user via service
        userService.addUser(user);

        // Retrieve user from DB to verify integration
        User registeredUser = userService.getUser("integration_user");
        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getId());
        
        // Verify password got encoded
        assertNotEquals("raw_password", registeredUser.getPassword());

        // Verify UserDetail was cascaded and saved correctly
        UserDetail persistedDetail = userDetailRepo.findById(registeredUser.getUserDetail().getId()).orElse(null);
        assertNotNull(persistedDetail);
        assertEquals("John", persistedDetail.getFirstName());
        assertEquals("Doe", persistedDetail.getLastName());
    }
}
