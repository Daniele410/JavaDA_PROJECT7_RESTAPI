package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.impl.RuleNameServiceImpl;
import com.nnk.springboot.service.impl.UserDetailServiceImpl;
import com.nnk.springboot.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    void showUserHome_shouldReturnModifiedView() throws Exception {
        //Given
        User user = new User("Gin", "GinTonic", "12345");
        when(userService.findAll()).thenReturn(Arrays.asList(user));

        //When
        mockMvc.perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"));
    }

    @Test
    void showAddUser_shouldReturnModifiedView() throws Exception {

        //When
        mockMvc.perform(get("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void validateAddUserForm_shouldReturnModifiedView() throws Exception {
        //Given
        User user = new User("Gin", "GinTonic", "12345");
        user.setRole("USER");
        when(userService.save(any())).thenReturn(user);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(user.getRole());
        //When
        mockMvc.perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", user.getUsername())
                        .param("fullname", user.getFullname())
                        .param("password", user.getPassword())
                        .param("role", user.getRole())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/list"));
    }

    @Test
    void validateAddUserForm_shouldReturn_hasError_ModifiedView() throws Exception {
        //Given
        User user = new User("Gin", "GinTonic", "12345");
        user.setRole("USER");
        when(userService.save(any())).thenReturn(user);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(user.getRole());
        //When
        mockMvc.perform(post("/user/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", user.getUsername())
                        .param("fullname", user.getFullname())
                        .param("password", user.getPassword())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    void showUpdateUserForm_shouldReturnModifiedView() throws Exception {
        //Given
        User user = new User("Gin", "GinTonic", "12345");
        user.setId(1);
        when(userService.findById(any())).thenReturn(Optional.of(user));

        //When
        mockMvc.perform(get("/user/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    @Test
    void updateUser_shouldReturnModifiedView() throws Exception {
        //Given
        User user = new User("Gin", "GinTonic", "12345");
        user.setId(1);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");

        when(userService.save(any())).thenReturn(user);

        //When
        mockMvc.perform(post("/user/update/{id}", 1)
                        .sessionAttr("user", user)
                        .param("username", "Gummy")
                        .param("fullname", "Gommo")
                        .param("password", user.getPassword())
                        .param("role", user.getRole())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/list"));
    }

    @Test
    void deleteUser() throws Exception {

        //Given
        User user = new User("Gin", "GinTonic", "12345");
        user.setId(1);

        doNothing().when(userService).delete(user.getId());

        //When
        mockMvc.perform(get("/user/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/list"));
    }
}