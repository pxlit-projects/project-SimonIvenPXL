package be.pxl.reviews.controller;

import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.repository.PostDraftRepository;
import be.pxl.reviews.domain.Review;
import be.pxl.reviews.enumdata.ReviewDecision;
import be.pxl.reviews.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ReviewControllerTest {
    private Review review1;
    private Review review2;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

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
        review1 = Review.builder()
                .postId(1L)
                .decision(ReviewDecision.APPROVED)
                .reasoning("Reasoning 1")
                .reviewEditor("Editor 1")
                .reviewedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        review2 = Review.builder()
                .postId(2L)
                .decision(ReviewDecision.REJECTED)
                .reasoning("Reasoning 2")
                .reviewEditor("Editor 2")
                .reviewedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();
    }

    @AfterEach
    public void tearDown() {
        reviewRepository.deleteAll();
    }

    @Test
    public void testGetAllDrafts() throws Exception {
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews"))
                .andExpect(status().isOk());

        Assertions.assertEquals(2, reviewRepository.findAll().size());
    }

    @Test
    public void testGetAllDraftsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews"))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, reviewRepository.findAll().size());
    }

    @Test
    public void testGetDraftById() throws Exception {
        reviewRepository.save(review1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/{id}", review1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDraftByIdEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
