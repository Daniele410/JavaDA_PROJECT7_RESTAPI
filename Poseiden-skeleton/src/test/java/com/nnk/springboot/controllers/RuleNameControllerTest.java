package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.impl.RuleNameServiceImpl;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(RuleNameController.class)
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameServiceImpl ruleNameService;

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    void showRuleNameHome_shouldReturnModifiedView() throws Exception {
        //Given
        RuleName ruleName = new RuleName("moody", "sandRating", "fitchRating", "template", "sqlStr", "sqlPart");
        when(ruleNameService.findAll()).thenReturn(Arrays.asList(ruleName));

        //When
        mockMvc.perform(get("/ruleName/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"));
    }

    @Test
    void showAddRuleForm_shouldReturnModifiedView() throws Exception {
        //Given
        RuleName ruleName = new RuleName("moody", "sandRating", "fitchRating", "template", "sqlStr", "sqlPart");

        //When
        mockMvc.perform(get("/ruleName/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void validateAddRuleName_shouldReturnModifiedView() throws Exception {
        //Given
        RuleName ruleName = new RuleName("moody", "NewDescription", "fitchRating", "template", "sqlStr", "sqlPart");

        when(ruleNameService.save(any())).thenReturn(ruleName);
        //When
        mockMvc.perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", ruleName.getName())
                        .param("description", ruleName.getDescription())
                        .param("json", ruleName.getJson())
                        .param("template", ruleName.getTemplate())
                        .param("sqlStr", ruleName.getSqlStr())
                        .param("sqlPart", ruleName.getSqlPart())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/ruleName/list"));
    }

    @Test
    void validateAddRuleName_shouldReturnErrorsModifiedView() throws Exception {
        //Given
        RuleName ruleName = new RuleName("moody", "sandRating", "fitchRating", "template", "sqlStr", "sqlPart");

        //When
        mockMvc.perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("moody", ruleName.getName())
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void showRuleNameUpdateForm_shouldReturnModifiedView() throws Exception {

        //Given
        RuleName ruleName = new RuleName("moody", "sandRating", "fitchRating", "template", "sqlStr", "sqlPart");
        ruleName.setId(1);
        when(ruleNameService.findById(any())).thenReturn(Optional.of(ruleName));

        //When
        mockMvc.perform(get("/ruleName/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));

    }

    @Test
    void updateRuleName_shouldReturnModifiedView() throws Exception {
        //Given
        RuleName ruleName = new RuleName("moody", "sandRating", "fitchRating", "template", "sqlStr", "sqlPart");
        ruleName.setId(1);

        when(ruleNameService.save(any())).thenReturn(ruleName);

        //When
        mockMvc.perform(post("/ruleName/update/{id}", 1)
                        .sessionAttr("ruleName", ruleName)
                        .param("name", "Gummy")
                        .param("description", ruleName.getDescription())
                        .param("json", ruleName.getJson())
                        .param("template", ruleName.getTemplate())
                        .param("sqlStr", ruleName.getSqlStr())
                        .param("sqlPart", ruleName.getSqlPart())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/ruleName/list"));
    }

    @Test
    void deleteRuleName() throws Exception {

        //Given
        RuleName ruleName = new RuleName("moody", "sandRating", "fitchRating", "template", "sqlStr", "sqlPart");
        ruleName.setId(1);
        doNothing().when(ruleNameService).delete(ruleName.getId());

        //When
        mockMvc.perform(get("/ruleName/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //Then
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/ruleName/list"));
    }
}