package org.example.testassignment.service;

import lombok.RequiredArgsConstructor;
import org.example.testassignment.entity.User;
import org.example.testassignment.exception.BirthdayRangeValidationException;
import org.example.testassignment.exception.ThereIsNoSuchUserException;
import org.example.testassignment.exception.UserWithThisAgeCanNotRegister;
import org.example.testassignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${user.registration.min-age}")
    private int minAge;

    public User createUser(User user) {
        if (!isUserOldEnough(user)) {
            throw new UserWithThisAgeCanNotRegister();
        }
        return userRepository.save(user);
    }


    public boolean isUserOldEnough(User user) {
        LocalDate now = LocalDate.now();
        LocalDate minAgeDate = now.minusYears(minAge);
        LocalDate userBirthDate = LocalDate.parse(user.getBirthDate().toString());
        return userBirthDate.isBefore(minAgeDate);
    }

    public String deleteUserById(Long id) {
        userRepository.deleteById(id);
        return "User delete";
    }

    public User updateUser(User user, Long id) {
        User updateUser = userRepository.findById(id).orElseThrow();
        if (updateUser == null) {
            throw new ThereIsNoSuchUserException();
        }

        updateUser.setEmail(user.getEmail());
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setAddress(user.getAddress());
        updateUser.setPhoneNumber(user.getPhoneNumber());
        updateUser.setBirthDate(user.getBirthDate());
        return userRepository.save(updateUser);
    }

    public List<User> getAllUsers(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new BirthdayRangeValidationException();
        }
        List<User> birthDate = userRepository.findByBirthDateBetween(fromDate, toDate);
        return birthDate;
    }
}
