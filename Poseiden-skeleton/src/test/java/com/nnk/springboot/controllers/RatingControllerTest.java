package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.impl.BidListServiceImpl;
import com.nnk.springboot.service.impl.RatingServiceImpl;
import com.nnk.springboot.service.impl.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RatingServiceImpl ratingService;
    @MockBean
    private UserDetailServiceImpl userDetailService;

    @MockBean
    private BindingResult bindingResult;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void showRatingHome_shouldReturnModifiedView() throws Exception {

        //Given
        Rating rating = new Rating("moody", "sandRating", "fitchRating", 1);
        when(ratingService.findAll()).thenReturn(Arrays.asList(rating));

        //When
        mockMvc.perform(get("/rating/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"));
    }

    @Test
    void addRatingForm_shouldReturnModifiedView() throws Exception {
        //When
        mockMvc.perform(get("/rating/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    void validateAddRating_shouldReturnModifiedView() throws Exception {
        //Given
        Rating rating = new Rating("moody", "sandRating", "fitchRating", 1);

        when(ratingService.save(any())).thenReturn(rating);

        //When
        mockMvc.perform(post("/rating/validate")
                        .sessionAttr("bidList", rating)
                        .param("moodysRating", rating.getMoodysRating())
                        .param("sandRating", rating.getSandRating())
                        .param("fitchRating", rating.getFitchRating())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    void showRatingUpdateForm_shouldReturnModifiedView() throws Exception {
        //Given
        Rating rating = new Rating("moody", "sandRating", "fitchRating", 1);
        when(ratingService.findById(any())).thenReturn(Optional.of(rating));

        //When
        mockMvc.perform(get("/rating/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    @Test
    void updateRating_shouldReturnErrors() throws Exception {

        //Given
        Rating rating = new Rating("moody", "sandRating", "fitchRating", 1);
//        when(ratingService.findById(any())).thenReturn(Optional.of(rating));
//
//        when(ratingService.update(any())).thenReturn(rating);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/rating/update/{id}", 3)
                        .sessionAttr("bidList", rating)
                        .param("moodysRating", rating.getMoodysRating())
                        .param("sandRating", rating.getSandRating())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/rating/update"));


    }

    @Test
    void updateRating_shouldReturnModifiedView() throws Exception {

        //Given
        Rating rating = new Rating("moody", "sandRating", "fitchRating", 1);
        rating.setId(1);
        when(ratingService.findById(any())).thenReturn(Optional.of(rating));
        when(ratingService.save(any())).thenReturn(rating);


        mockMvc.perform(post("/rating/validate")
                .sessionAttr("bidList", rating)
                .param("moodysRating", rating.getMoodysRating())
                .param("sandRating", rating.getSandRating())
                .param("fitchRating", rating.getFitchRating())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //When
        mockMvc.perform(post("/rating/update/{id}", 1)
                        .sessionAttr("rating", rating)
                        .param("id", "1")
                        .param("moodysRating", "newRating")
                        .param("sandRating", "newSendRating")
                        .param("fitchRating", "newFitchRating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/rating/list"));


    }

    @Test
    void deleteRating() throws Exception {
        //Given
        Rating rating = new Rating("moody", "sandRating", "fitchRating", 1);
        rating.setId(1);
        doNothing().when(ratingService).delete(rating.getId());
        //When
        mockMvc.perform(get("/rating/delete/{id}", 1)
                        .sessionAttr("rating", rating)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/rating/list"));
    }
}