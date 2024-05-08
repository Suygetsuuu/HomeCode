package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.dto.UserResourcesDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String ADMIN_ENCRYPTED = "eyJhY2NvdW50TmFtZSI6ImFhYSIsInJvbGUiOiJhZG1pbiIsInVzZXJJZCI6MTExMX0";
    private static final String USER_123456_ENCRYPTED = "eyJhY2NvdW50TmFtZSI6ImFhYSIsInJvbGUiOiJ1c2VyIiwidXNlcklkIjoxMjM0NTZ9";
    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testAddUser() throws Exception{
        UserResourcesDTO userResourcesDTO = new UserResourcesDTO();
        userResourcesDTO.setUserId(123L);
        List<String> endpoint = new ArrayList<>();
        endpoint.add("resource H");
        userResourcesDTO.setEndpoint(endpoint);

        // 调用Controller的方法
        MvcResult mvcResult = mockMvc.perform(post("/admin/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", ADMIN_ENCRYPTED)
                .content(JSONObject.toJSONString(userResourcesDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }

    @Test
    public void testUserResources() throws Exception{
        // 调用Controller的方法
        MvcResult mvcResult = mockMvc.perform(get("/user/{resource}", "resource A")
                .header("Authorization", USER_123456_ENCRYPTED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }
}
