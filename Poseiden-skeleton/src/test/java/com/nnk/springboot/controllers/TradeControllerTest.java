package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.impl.TradeServiceImpl;
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

import javax.naming.Binding;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TradeController.class)
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeServiceImpl tradeService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    BindingResult bindingResult;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    void showTradeHome_shouldReturnModifiedView() throws Exception {
        //Given
        Trade trade = new Trade("NewAccount","newType",1.0);
        when(tradeService.findAll()).thenReturn(Arrays.asList(trade));
        //When
        mockMvc.perform(get("/trade/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"));


    }

    @Test
    void showAddTradeForm_shouldReturnModifiedView() throws Exception {

        //When
        mockMvc.perform(get("/trade/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void validateForm_shouldReturnModifiedView() throws Exception {
        //Given
        Trade trade = new Trade("NewAccount","newType",1.0);
        when(tradeService.findAll()).thenReturn(Arrays.asList(trade));
        //When
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("account", trade.getAccount())
                        .param("type",trade.getType())
                        .param("buyQuantity","1.0")
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trade/list"));
    }


    @Test
    void showUpdateForm_shouldReturnModifiedViewError() throws Exception {
        Trade trade = new Trade("NewAccount","newType",1.0);
        trade.setTradeId(1);
        when(tradeService.findById(any())).thenReturn(Optional.of(trade));
        mockMvc.perform(get("/trade/update/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }


    @Test
    void updateTradeForm_shouldReturnModifiedView() throws Exception {
        Trade trade = new Trade("NewAccount","newType",1.0);
        trade.setTradeId(1);
        when(tradeService.save(any())).thenReturn(trade);
        mockMvc.perform(post("/trade/update/{id}",1)
                        .param("account", trade.getAccount())
                        .param("type","newType2")
                        .param("buyQuantity","3.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    void updateTradeForm_shouldReturnErrorModifiedView() throws Exception {
        Trade trade = new Trade("NewAccount","newType",1.0);
        trade.setTradeId(1);

        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/trade/update/{id}",1)
                        .param("account", trade.getAccount())
                        .param("type","newType2")

                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }


    @Test
    void deleteTrade_shouldReturnErrorModifiedView() throws Exception {

        Trade trade = new Trade("NewAccount","newType",1.0);
        trade.setTradeId(1);
        when(tradeService.findAll()).thenReturn(List.of(trade));
        mockMvc.perform(get("/trade/delete/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trade/list"));
    }
}