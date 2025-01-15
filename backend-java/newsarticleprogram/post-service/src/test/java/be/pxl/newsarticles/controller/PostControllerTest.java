package be.pxl.newsarticles.controller;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.repository.PostRepository;
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
public class PostControllerTest {
    private Post post1;
    private Post post2;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

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
        post1 = Post.builder()
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer 1")
                .reviewReasoning("Reasoning 1")
                .commentIds(List.of())
                .publishedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        post2 = Post.builder()
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer 2")
                .reviewReasoning("Reasoning 2")
                .commentIds(List.of())
                .publishedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();
    }

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
    }

    @Test
    public void testCreatePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post1)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, postRepository.findAll().size());
    }

    @Test
    public void testGetAllPosts() throws Exception {
        postRepository.save(post1);
        postRepository.save(post2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(status().isOk());

        Assertions.assertEquals(2, postRepository.findAll().size());
    }

    @Test
    public void testGetAllPostsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, postRepository.findAll().size());
    }

    @Test
    public void testGetPostById() throws Exception {
        postRepository.save(post1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", post1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPostByIdEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveEditsToPost() throws Exception {
        postRepository.save(post1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", post1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveEditsToPostEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post1)))
                .andExpect(status().isNotFound());
    }



}
