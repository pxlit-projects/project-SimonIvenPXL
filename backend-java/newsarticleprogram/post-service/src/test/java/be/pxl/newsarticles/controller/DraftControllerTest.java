package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.repository.PostDraftRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class DraftControllerTest {
    private Draft draft1;
    private Draft draft2;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostDraftRepository draftRepository;

    @Container
    private static final MariaDBContainer<?> mariaDbContainer = new MariaDBContainer<>("mariadb:latest");

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDbContainer::getUsername);
        registry.add("spring.datasource.password", mariaDbContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        draft1 = Draft.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        draft2 = Draft.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .status(PostStatus.DRAFT)
                .savedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();
    }

    @AfterEach
    public void tearDown() {
        draftRepository.deleteAll();
    }

    @Test
    public void testCreateDraft() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/drafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draft1)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, draftRepository.findAll().size());
    }

    @Test
    public void testGetAllDrafts() throws Exception {
        draftRepository.save(draft1);
        draftRepository.save(draft2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drafts"))
                .andExpect(status().isOk());

        Assertions.assertEquals(2, draftRepository.findAll().size());
    }

    @Test
    public void testGetAllDraftsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/drafts"))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, draftRepository.findAll().size());
    }

    @Test
    public void testGetDraftById() throws Exception {
        draftRepository.save(draft1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drafts/{id}", draft1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDraftByIdEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/drafts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveEditsToDraft() throws Exception {
        draftRepository.save(draft1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/drafts/{id}", draft1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draft1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveEditsToDraftEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/drafts/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(draft1)))
                .andExpect(status().isNotFound());
    }
}
