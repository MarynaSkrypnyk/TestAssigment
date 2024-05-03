package org.example.testassignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.testassignment.dto.UserDto;
import org.example.testassignment.entity.User;
import org.example.testassignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    private static final String URL_TEMPLATE = "/users";

    private final LocalDate date = LocalDate.of(2000, 12, 12);

    @BeforeEach
    void setUp() {
        user = new User(1L, "Mary@gmail.com", "Mary", "Mary", date, "Lviv", 999);
    }

    @Test
    public void save_user() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post(URL_TEMPLATE + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mary"))
                .andExpect(jsonPath("$.lastName").value("Mary"))
                .andExpect(jsonPath("$.email").value("Mary@gmail.com"))
                .andExpect(jsonPath("$.address").value("Lviv"))
                .andExpect(jsonPath("$.phoneNumber").value(999))
                .andExpect(jsonPath("$.birthDate").value(date.toString()));

    }

    @Test
    public void delete_user() throws Exception {
        mockMvc.perform(delete(URL_TEMPLATE + "/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User delete"));

        verify(userService).deleteUserById(user.getId());
    }

    @Test
    public void update_user() throws Exception {
        UserDto userDto = new UserDto("Ben@gmail.com", "Ben", "Ben", date, "Mykolaiv", 7777);

        User updatedUser = new User();
        updatedUser.setEmail(userDto.getEmail());
        updatedUser.setFirstName(userDto.getFirstName());
        updatedUser.setLastName(userDto.getLastName());
        updatedUser.setBirthDate(userDto.getBirthDate());
        updatedUser.setPhoneNumber(userDto.getPhoneNumber());
        updatedUser.setAddress(userDto.getAddress());
        when(userService.updateUser(any(User.class), any(Long.class))).thenReturn(updatedUser);

        mockMvc.perform(put(URL_TEMPLATE + "/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email").value("Ben@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Ben"))
                .andExpect(jsonPath("$.lastName").value("Ben"))
                .andExpect(jsonPath("$.address").value("Mykolaiv"))
                .andExpect(jsonPath("$.phoneNumber").value(7777))
                .andExpect(jsonPath("$.birthDate").value(date.toString()));
    }

    @Test
    public void get_all_users_by_birthdate_range() throws Exception {
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

        when(userService.getAllUsers(fromDate, toDate)).thenReturn(expectedUsers);

        mockMvc.perform(get(URL_TEMPLATE + "/list")
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedUsers.size()));
    }
}

