package org.example.testassignment.repository;

import org.example.testassignment.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;
    private User user;
    private final LocalDate date = LocalDate.of(2000, 12, 12);

    @BeforeEach
    void setUp() {
        user = new User(1L, "Mary@gmail.com", "Mary", "Mary", date, "Lviv", 999);
    }

    @Test
    public void testFindUserById() {
        when(userRepository.findUserById(user.getId())).thenReturn(user);
        User foundUser = userRepository.findUserById(user.getId());
        assertEquals(user, foundUser);
        verify(userRepository).findUserById(user.getId());
    }

    @Test
    public void testFindByBirthDateBetween() {
        LocalDate firstDate = LocalDate.of(2000, 12, 12);
        LocalDate secondDate = LocalDate.of(1978, 12, 12);
        LocalDate thirdDate = LocalDate.of(2005, 12, 12);

        LocalDate fromDate = LocalDate.of(1999, 12, 12);
        LocalDate toDate = LocalDate.of(2000, 12, 12);

        User firstUser = new User(1L, "Jack@gmail.com", "Jack", "Jack", firstDate, "Lviv", 5555);
        User secondUser = new User(2L, "Oleh@gmail.com", "Oleh", "Oleh", secondDate, "Lviv", 5555);
        User thirdUser = new User(2L, "Ann@gmail.com", "Ann", "Ann", thirdDate, "Lviv", 5555);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(firstUser);
        expectedUsers.add(secondUser);
        expectedUsers.add(thirdUser);

        when(userRepository.findByBirthDateBetween(fromDate, toDate)).thenReturn(expectedUsers);
        List<User> foundUsers = userRepository.findByBirthDateBetween(fromDate, toDate);
        assertEquals(expectedUsers, foundUsers);
        verify(userRepository).findByBirthDateBetween(fromDate, toDate);
    }
}