package com.nnk.springboot.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.impl.BidListServiceImpl;
import com.nnk.springboot.service.impl.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BidListController.class)
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BidListServiceImpl bidListService;
    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    void showBidListShouldReturnModifiedBidListView() throws Exception {
        //Given
        BidList bid = new BidList("Account", "Type", 5d);

        when(bidListService.findAll()).thenReturn(Arrays.asList(bid));
        //When
        mockMvc.perform(get("/bidList/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/list"));
    }

    @Test
    void addBidForm() throws Exception {

        //Given
        BidList bid = new BidList("Account", "Type", 5d);

        when(bidListService.findAll()).thenReturn(Arrays.asList(bid));
        //When
        mockMvc.perform(get("/bidList/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/add"));


    }

    @Test
    void validateAddBid_shouldReturnError() throws Exception {

        //Given
        BidList bid = new BidList("Account", "Type", 5d);

        //GIVEN
        JSONObject json = new JSONObject();
        json.put("account", "toto");
        json.put("type", "tutu");
        json.put("bidQuantity", 20);

        when(bidListService.save(any())).thenReturn(bid);
        //When
        mockMvc.perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("/bidList/add"));

    }

    @Test
    void validateAddBid_shouldReturnModifiedBidListView() throws Exception {

        //Given
        BidList bid = new BidList("Account", "Type", 5d);

        when(bidListService.update(any())).thenReturn(bid);

        //When
        mockMvc.perform(post("/bidList/validate")
                        .sessionAttr("bidList", bid)
                        .param("account", bid.getAccount())
                        .param("type", bid.getType())
                        .param("bidQuantity", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(view().name("redirect:/bidList/list"));

    }

    @Test
    void showUpdateForm_shouldReturnModifiedBidListView() throws Exception {

        //Given
        BidList bid = new BidList("Account", "Type", 5d);

        when(bidListService.findById(any())).thenReturn(Optional.of(bid));
        //When
        mockMvc.perform(get("/bidList/update/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/update"));
    }

    @Test
    void updateBid_shouldReturnModifiedBidListView() throws Exception {
        //Given
        BidList bid = new BidList("Account", "Type", 5d);
        bid.setBidListId(1);
        when(bidListService.update(any())).thenReturn(bid);

        //When
        mockMvc.perform(post("/bidList/validate")
                .sessionAttr("bidList", bid)
                .param("account", bid.getAccount())
                .param("type", bid.getType())
                .param("bidQuantity", "1.0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/bidList/update/1")
                        .sessionAttr("bidList", bid)
                        .param("account", "NewAccount")
                        .param("type", bid.getType())
                        .param("bidQuantity", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/bidList/list"));

    }



    @Test
    void deleteBid() throws Exception {

        //Given
        BidList bid = new BidList("Account", "Type", 5d);
        bid.setBidListId(1);
        doNothing().when(bidListService).delete(bid.getBidListId());

        //When
        mockMvc.perform(post("/bidList/validate")
                .sessionAttr("bidList", bid)
                .param("account", bid.getAccount())
                .param("type", bid.getType())
                .param("bidQuantity", "1.0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/bidList/delete/{id}",1)
                        .sessionAttr("bidList", bid)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/bidList/list"));
    }
}