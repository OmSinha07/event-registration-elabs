package org.elabs.eventregistration;

import org.elabs.eventregistration.user.User;
import org.elabs.eventregistration.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepoTests {
    @Autowired private UserRepository repo;

    @Test
    public void testAddNew() {
        User user = new User();
        user.setEmail("user123@gmail.com");
        user.setPassword("user1234");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRoll(1234);
        User savedUser = repo.save(user);
        Assertions.assertNotNull(savedUser, "User was not saved");
    }

    @Test
    public void testListAll(){
        Iterable<User> users = repo.findAll();
        List<User> userList = new ArrayList<>();
        users.forEach(userList::add);
        Assertions.assertFalse(userList.isEmpty(), "User list should not be empty");
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void testUpdate() {
        Integer userID = 1;
        Optional<User> optionalUser = repo.findById(userID);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword("user@4567");
            repo.save(user);
            Optional<User> updatedUserOptional = repo.findById(userID);
            if (updatedUserOptional.isPresent()) {
                User updatedUser = updatedUserOptional.get();
                Assertions.assertEquals("user@4567", updatedUser.getPassword(), "Password should be updated");
            } else {
                System.out.println("Updated user not found");
            }
        } else {
            System.out.println("User not found");
        }
    }

    @Test
    public void testGet() {
        Integer userID = 1;
        Optional<User> optionalUser = repo.findById(userID);
        Assertions.assertTrue(optionalUser.isPresent(), "User should be present");
    }

    @Test
    public void testDelete() {
        Integer userID = 1;
        repo.deleteById(userID);
        Optional<User> optionalUser = repo.findById(userID);
        Assertions.assertFalse(optionalUser.isPresent(), "User should not be present");
    }
}
