package fikv.ariseth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fikv.ariseth.dtos.UserRequestDTO;
import fikv.ariseth.entities.User;
import fikv.ariseth.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register() throws Exception {
        UserRequestDTO dto = new UserRequestDTO("User", "password", "email@example.com");
        User user = new User();

        given(userService.create(any(UserRequestDTO.class))).willReturn(user);

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void login() throws Exception {
        UserRequestDTO dto = new UserRequestDTO("User", "password", "email@example.com");

        given(userService.verify(any(UserRequestDTO.class)).token()).willReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("token"));
    }

    @Test
    void getById() throws Exception {
        User user = new User();

        given(userService.getById(1L)).willReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        given(userService.getAll()).willReturn(List.of(new User()));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        User user = new User();

        given(userService.update(eq(1L), any(User.class))).willReturn(user);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk());
    }
}
