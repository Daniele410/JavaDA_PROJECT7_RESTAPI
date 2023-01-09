package com.nnk.springboot.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.IUserService;
import com.nnk.springboot.service.impl.RatingServiceImpl;
import com.nnk.springboot.service.impl.UserDetailServiceImpl;
import com.nnk.springboot.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService;
    @MockBean
    private UserDetailServiceImpl userDetailService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;



    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void userRegistrationDto() {
    }

    @Test
    void showRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));

    }

    @Test
    void registerUserAccount() throws Exception{

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("Mario");
        registrationDto.setFullname("Mario");
        registrationDto.setPassword("1Qwertyuiop!");
        String cryptPassword = passwordEncoder.encode((registrationDto.getPassword()));
        User user = new User(registrationDto.getUsername(),
                registrationDto.getFullname(),
                cryptPassword,registrationDto.getRole());

        when(userService.saveUser(registrationDto,cryptPassword)).thenReturn(user);

        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());
        json.put("fullname", user.getFullname());
        json.put("role", user.getRole());


        //When
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/registration?success"));

    }
}