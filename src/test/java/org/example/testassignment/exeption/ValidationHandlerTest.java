package org.example.testassignment.exeption;

import org.example.testassignment.entity.User;
import org.example.testassignment.exception.BirthdayRangeValidationException;
import org.example.testassignment.exception.ThereIsNoSuchUserException;
import org.example.testassignment.exception.UserWithThisAgeCanNotRegister;
import org.example.testassignment.repository.UserRepository;
import org.example.testassignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidationHandlerTest {
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
    public void is_user_with_this_id_exist() {
        Long nonExistentId = 123L;
        assertThrows(ThereIsNoSuchUserException.class, () -> userService.updateUser(user, nonExistentId));

        verify(userRepository, times(1)).findUserById(nonExistentId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void get_all_users_with_invalid_birthdate_range() {
        LocalDate fromDate = LocalDate.of(2000, 1, 1);
        LocalDate toDate = LocalDate.of(1999, 1, 1);

        assertThrows(BirthdayRangeValidationException.class, () -> userService.getAllUsers(fromDate, toDate));
    }

    @Test
    public void user_age_verification() {
        LocalDate birthday = LocalDate.of(2024, 12, 12);
        user.setBirthDate(birthday);
        assertThrows(UserWithThisAgeCanNotRegister.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }
}
