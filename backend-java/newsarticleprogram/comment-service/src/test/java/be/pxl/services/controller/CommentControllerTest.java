package be.pxl.services.controller;

import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CommentControllerTest {
    private Comment comment1;
    private Comment comment2;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

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
        comment1 = Comment.builder()
                .postId(1L)
                .content("Reasoning 1")
                .author("Editor 1")
                .commentDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        comment2 = Comment.builder()
                .postId(2L)
                .content("Reasoning 2")
                .author("Editor 2")
                .commentDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();
    }

    @AfterEach
    public void tearDown() {
        commentRepository.deleteAll();
    }

    @Test
    public void testCreateComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment1)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    public void testGetAllComments() throws Exception {
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments"))
                .andExpect(status().isOk());

        Assertions.assertEquals(2, commentRepository.findAll().size());
    }

    @Test
    public void testGetAllCommentsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments"))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    public void testGetCommentById() throws Exception {
        commentRepository.save(comment1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{id}", comment1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCommentByIdEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
