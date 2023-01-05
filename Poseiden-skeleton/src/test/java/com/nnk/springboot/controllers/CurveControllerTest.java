package com.nnk.springboot.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.impl.CurvePointServiceImpl;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CurveController.class)
class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurvePointServiceImpl curvePointService;
    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    void home_showAllCurvePoint_ShouldReturnModifiedCurvePointListView() throws Exception {
        //Given

        CurvePoint curvePoint = new CurvePoint(1,1,1.0,1.0);

        when(curvePointService.findAll()).thenReturn(Arrays.asList(curvePoint));
        //When
        mockMvc.perform(get("/curvePoint/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(view().name("curvePoint/list"));
    }

    @Test
    void showAddCurvePointForm_ShouldReturnModifiedCurvePointView() throws Exception {
        mockMvc.perform(get("/curvePoint/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));

    }

    @Test
    void validateCurvePointForm_ShouldReturnModifiedCurvePointView() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1,1,1.0,1.0);
        JSONObject json = new JSONObject();
        json.put("curveId", curvePoint.getCurveId());
        json.put("term", curvePoint.getTerm());
        json.put("value", curvePoint.getValue());


        when(curvePointService.save(curvePoint)).thenReturn(curvePoint);
        mockMvc.perform(post("/curvePoint/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/curvePoint/list"));
    }

    @Test
    void showUpdateForm() {
    }

    @Test
    void updateCurvePoint() {
    }

    @Test
    void deleteCurvePoint() {
    }
}