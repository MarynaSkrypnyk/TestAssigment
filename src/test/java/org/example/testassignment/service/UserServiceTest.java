package org.example.testassignment.service;

import org.example.testassignment.entity.User;
import org.example.testassignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private User user;
    private int minAge = 18;
    private final LocalDate date = LocalDate.of(2000, 12, 12);

    @BeforeEach
    void setUp() {
        user = new User(1L, "Mary@gmail.com", "Mary", "Mary", date, "Lviv", 999);
    }

    @Test
    public void create_user() {
        when(userRepository.save(user)).thenReturn(user);
        LocalDate now = LocalDate.now();
        LocalDate minAgeDate = now.minusYears(minAge);
        LocalDate userBirthDate = LocalDate.parse(user.getBirthDate().toString());
        User savedUser = userService.createUser(user);

        assertTrue(userBirthDate.isBefore(minAgeDate));
        assertNotNull(savedUser);
        assertSame(user, savedUser);
    }

    @Test
    public void is_user_old_enough() {
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        user.setBirthDate(birthday);
        assertTrue(userService.isUserOldEnough(user));

        LocalDate notOldEnoughUserBirthday = LocalDate.of(2024, 12, 12);
        User notOldEnoughUser = new User();
        notOldEnoughUser.setBirthDate(notOldEnoughUserBirthday);
        assertFalse(userService.isUserOldEnough(notOldEnoughUser));
    }

    @Test
    public void delete_user() {
        String deleteUser = userService.deleteUserById(user.getId());
        assertEquals(deleteUser, "User delete");
    }

    @Test
    public void update_user_by_id() {
        LocalDate testDate = LocalDate.of(2000, 12, 12);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        user.setEmail("mail@mail.com");
        user.setFirstName("Dan");
        user.setLastName("Dan");
        user.setAddress("Mykolaiv");
        user.setPhoneNumber(8888);
        user.setBirthDate(testDate);

        when(userRepository.save(user)).thenReturn(user);
        User result = userService.updateUser(user, user.getId());
        assertEquals(user, result);
    }

    @Test
    public void get_all_users() {
        LocalDate firstDate = LocalDate.of(2000, 12, 12);
        LocalDate secondDate = LocalDate.of(1978, 12, 12);
        LocalDate thirdDate = LocalDate.of(2005, 12, 12);

        LocalDate fromDate = LocalDate.of(1995, 12, 12);
        LocalDate toDate = LocalDate.of(1998, 12, 12);

        User firstUser = new User(1L, "Jack@gmail.com", "Jack", "Jack", firstDate, "Lviv", 5555);
        User secondUser = new User(2L, "Oleh@gmail.com", "Oleh", "Oleh", secondDate, "Lviv", 5555);
        User thirdUser = new User(2L, "Ann@gmail.com", "Ann", "Ann", thirdDate, "Lviv", 5555);

        List<User> userList = new ArrayList<>();
        userList.add(firstUser);
        userList.add(secondUser);
        userList.add(thirdUser);

        when(userRepository.findByBirthDateBetween(fromDate, toDate)).thenReturn(userList);

        List<User> result = userService.getAllUsers(fromDate, toDate);

        assertTrue(fromDate.isBefore(toDate));
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(userList, result);
    }
}

