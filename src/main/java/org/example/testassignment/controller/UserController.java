package org.example.testassignment.controller;

import lombok.RequiredArgsConstructor;
import org.example.testassignment.dto.UserDto;
import org.example.testassignment.entity.User;
import org.example.testassignment.exception.BirthdayRangeValidationException;
import org.example.testassignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/create")
    public User createNewUser(@RequestBody UserDto userDto) {
        User user = setUserField(userDto);
        return userService.createUser(user);
    }


    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User delete";
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        User updateUser = setUserField(userDto);
        User userUpdate = userService.updateUser(updateUser, id);
        return userUpdate;
    }

    private User setUserField(@RequestBody UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthDate(userDto.getBirthDate());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        return user;
    }

    @GetMapping("/list")
    public List<User> getUsersByBirthDateRange(
            @RequestParam("fromDate") LocalDate fromDate,
            @RequestParam("toDate") LocalDate toDate) {


        List<User> allUsers = userService.getAllUsers(fromDate, toDate);
        return allUsers;
    }
}
