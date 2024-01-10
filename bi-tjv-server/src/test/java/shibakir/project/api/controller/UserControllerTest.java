package shibakir.project.api.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.hamcrest.Matchers;
import shibakir.project.api.controller.UserController;
import shibakir.project.api.dto.UserDTO;
import shibakir.project.business.UserService;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Test
    public void checkMethod_Create() throws Exception {
        UserEntity user = new UserEntity("username", "password");
        when(userService.create(user)).thenReturn(user);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("username")))
                .andExpect(jsonPath("$.password", Matchers.is("password")));
    }

    @Test
    public void checkMethod_Update() throws Exception {
        UserEntity user = new UserEntity("username", "password");
        when(userService.update(user)).thenReturn(user);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(put("/users/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("username")))
                .andExpect(jsonPath("$.password", Matchers.is("password")));
    }

    @Test
    public void checkMethod_Delete() throws Exception {
        UserEntity user = new UserEntity("username", "password");

        when(userService.readById(not(eq("username")))).thenReturn(Optional.empty());
        when(userService.readById("username")).thenReturn(Optional.of(user));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(delete("/users/notexistingUserId")).andExpect(status().isNotFound());

        verify(userService, never()).deleteById(any());

        mockMvc.perform(delete("/users/username")).andExpect(status().isOk());
        verify(userService, times(1)).deleteById("username");
    }

    @Test
    public void checkMethod_GetOne() throws Exception {
        UserEntity user = new UserEntity("username", "password");
        when(userService.readById("username")).thenReturn(Optional.of(user));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users/username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("username")))
                .andExpect(jsonPath("$.password", Matchers.is("password")));

        when(userService.readById(not(eq("username")))).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/notexistingUserId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkMethod_GetAllUsers() throws Exception {
        List<UserEntity> users = List.of(new UserEntity("user1", "password1"), new UserEntity("user2", "password2"));
        when(userService.readAll()).thenReturn(users);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].username", Matchers.is("user1")))
                .andExpect(jsonPath("$[1].username", Matchers.is("user2")));
    }
}

