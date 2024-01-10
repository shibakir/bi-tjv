package shibakir.project.api.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shibakir.project.api.dto.MemeDTO;
import shibakir.project.business.MemeService;
import shibakir.project.domain.MemeEntity;
import org.mockito.ArgumentCaptor;
import shibakir.project.domain.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemeControllerTest {

    @Mock
    private MemeService memeService;

    @InjectMocks
    private MemeController memeController;

    private MockMvc mockMvc;

    @Test
    public void checkMethod_Create() throws Exception {
        MemeEntity meme = new MemeEntity("name", "text");

        when(memeService.create(meme)).thenReturn(meme);

        mockMvc = MockMvcBuilders.standaloneSetup(memeController).build();

        mockMvc.perform(post("/memes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"name\",\"path_name\":\"text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("name")))
                .andExpect(jsonPath("$.path_name", Matchers.is("text")));

        ArgumentCaptor<MemeEntity> argumentCaptor = ArgumentCaptor.forClass(MemeEntity.class);
        verify(memeService, times(1)).create(argumentCaptor.capture());
        MemeEntity providedMeme = argumentCaptor.getValue();
        assertEquals("name", providedMeme.getName());
        assertEquals("text", providedMeme.getPath_name());
    }

    @Test
    public void checkMethod_Update() throws Exception {
        MemeEntity meme = new MemeEntity("name", "text");

        when(memeService.update(meme)).thenReturn(meme);

        mockMvc = MockMvcBuilders.standaloneSetup(memeController).build();

        mockMvc.perform(put("/memes/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"name\",\"path_name\":\"text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("name")))
                .andExpect(jsonPath("$.path_name", Matchers.is("text")));

        ArgumentCaptor<MemeEntity> argumentCaptor = ArgumentCaptor.forClass(MemeEntity.class);
        verify(memeService, times(1)).update(argumentCaptor.capture());
        MemeEntity providedMeme = argumentCaptor.getValue();
        assertEquals("name", providedMeme.getName());
        assertEquals("text", providedMeme.getPath_name());
    }

    @Test
    public void checkMethod_Delete() throws Exception {
        MemeEntity meme = new MemeEntity("name", "text");

        when(memeService.readById(not(eq("name")))).thenReturn(Optional.empty());
        when(memeService.readById("name")).thenReturn(Optional.of(meme));

        mockMvc = MockMvcBuilders.standaloneSetup(memeController).build();

        mockMvc.perform(delete("/memes/notexistingMemeId")).andExpect(status().isNotFound());

        verify(memeService, never()).deleteById(any());

        mockMvc.perform(delete("/memes/name")).andExpect(status().isOk());
        verify(memeService, times(1)).deleteById("name");
    }

    @Test
    public void checkMethod_GetOne() throws Exception {
        MemeEntity meme = new MemeEntity("name", "text");
        when(memeService.readById("name")).thenReturn(Optional.of(meme));

        mockMvc = MockMvcBuilders.standaloneSetup(memeController).build();

        mockMvc.perform(get("/memes/name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("name")))
                .andExpect(jsonPath("$.path_name", Matchers.is("text")));

        when(memeService.readById(not(eq("name")))).thenReturn(Optional.empty());

        mockMvc.perform(get("/memes/notexistingMemeId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkMethod_GetAll() throws Exception {
        MemeEntity meme1 = new MemeEntity("name1", "text");
        MemeEntity meme2 = new MemeEntity("name2", "text");
        List<MemeEntity> memes = List.of(meme1, meme2);

        when(memeService.readAll()).thenReturn(memes);

        mockMvc = MockMvcBuilders.standaloneSetup(memeController).build();

        mockMvc.perform(get("/memes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("name1")))
                .andExpect(jsonPath("$[1].name", Matchers.is("name2")));
    }
}