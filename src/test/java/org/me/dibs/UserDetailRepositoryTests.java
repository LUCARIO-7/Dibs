package org.me.dibs;

import org.junit.jupiter.api.Test;
import org.me.dibs.Repository.UserDetailRepository;
import org.me.dibs.Repository.UserRepository;
import org.me.dibs.model.User;
import org.me.dibs.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserDetailRepositoryTests {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserDetailRepository userDetailRepo;

    @Test
    public void testSaveUserDetailCascade() {
        User user = new User();
        user.setUsername("cascade_user");
        user.setPassword("password123");

        UserDetail detail = new UserDetail();
        detail.setFirstName("Alice");
        detail.setLastName("Smith");
        detail.setEmail("alice@example.com");
        detail.setPhoneNumber("1234567890");
        detail.setAddress("123 Main St");
        detail.setUser(user);

        user.setUserDetail(detail);

        // Save User - this should cascade to UserDetail
        User savedUser = userRepo.save(user);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getUserDetail());
        assertNotNull(savedUser.getUserDetail().getId());
        assertEquals("Alice", savedUser.getUserDetail().getFirstName());

        // Check if detail is actually persisted in database
        UserDetail persistedDetail = userDetailRepo.findById(savedUser.getUserDetail().getId()).orElse(null);
        assertNotNull(persistedDetail);
        assertEquals(savedUser.getId(), persistedDetail.getUser().getId());
    }

    @Test
    public void testDeleteUserCascade() {
        User user = new User();
        user.setUsername("delete_user");
        user.setPassword("password123");

        UserDetail detail = new UserDetail();
        detail.setFirstName("Bob");
        detail.setLastName("Jones");
        detail.setEmail("bob@example.com");
        detail.setUser(user);

        user.setUserDetail(detail);

        User savedUser = userRepo.save(user);
        Integer userId = savedUser.getId();
        Integer detailId = savedUser.getUserDetail().getId();

        // Verify exist
        assertTrue(userRepo.existsById(userId));
        assertTrue(userDetailRepo.existsById(detailId));

        // Delete User
        userRepo.delete(savedUser);

        // Verify cascade delete
        assertFalse(userRepo.existsById(userId));
        assertFalse(userDetailRepo.existsById(detailId));
    }

    @Test
    public void testUniqueUserConstraint() {
        User user = new User();
        user.setUsername("unique_user");
        user.setPassword("password123");
        userRepo.save(user);

        UserDetail detail1 = new UserDetail();
        detail1.setFirstName("First");
        detail1.setUser(user);
        userDetailRepo.save(detail1);

        // Create second detail for same user
        UserDetail detail2 = new UserDetail();
        detail2.setFirstName("Second");
        detail2.setUser(user);

        // Attempting to save a second detail with same user_id should fail unique constraint
        assertThrows(DataIntegrityViolationException.class, () -> {
            userDetailRepo.saveAndFlush(detail2);
        });
    }
}
