package com.nnk.springboot.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nnk.springboot.domain.BidList;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
        CurvePoint curvePoint = new CurvePoint(1, 1, 1.0, 1.0);
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
        //When
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
        //Given
        CurvePoint curvePoint = new CurvePoint(1, 1, 1.0, 1.0);
        JSONObject json = new JSONObject();
        json.put("curveId", curvePoint.getCurveId());
        json.put("term", curvePoint.getTerm());
        json.put("value", curvePoint.getValue());

        when(curvePointService.save(curvePoint)).thenReturn(curvePoint);

        //When
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
    void showUpdateCurvePointForm_shouldReturnModifiedCurvePointView() throws Exception {

        //Given
        CurvePoint curvePoint = new CurvePoint(1, 1, 1.0, 1.0);
        curvePoint.setId(1);

        when(curvePointService.findById(any())).thenReturn(Optional.of(curvePoint));

        //When
        mockMvc.perform(get("/curvePoint/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    void updateCurvePoint_shouldReturnModifiedCurvePointView() throws Exception {

        //Given
        CurvePoint curvePoint = new CurvePoint(1, 1, 1.0, 1.0);
        curvePoint.setId(1);

        JSONObject json = new JSONObject();
        json.put("id", curvePoint.getId());
        json.put("curveId", curvePoint.getCurveId());
        json.put("term", curvePoint.getTerm());
        json.put("value", curvePoint.getValue());

        when(curvePointService.findAll()).thenReturn(List.of(curvePoint));
        when(curvePointService.save(curvePoint)).thenReturn(curvePoint);
        //When
        mockMvc.perform(post("/curvePoint/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(json.toString())
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/curvePoint/list"));

    }

    @Test
    void deleteCurvePoint() throws Exception {
        //Given
        CurvePoint curvePoint = new CurvePoint(1, 1, 1.0, 1.0);

        //When
        mockMvc.perform(get("/curvePoint/delete/{id}", 1)
                        .sessionAttr("curvePoint", curvePoint)
                        .accept(MediaType.APPLICATION_JSON))

                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/curvePoint/list"));
    }
}