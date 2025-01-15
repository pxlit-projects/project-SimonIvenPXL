package be.pxl.newsarticles.service;

import be.pxl.newsarticles.client.CommentClient;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.repository.PostRepository;
import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    private List<Long> commentIds1;
    private List<Long> commentIds2;

    private Post post1;
    private Post post2;
    private Comment comment1;
    private Comment comment2;

    private PostResponse response1;
    private PostResponse response2;
    private CommentResponse commentResponse1;
    private CommentResponse commentResponse2;

    private PostRequest request;
    private CommentRequest commentRequest;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private CommentClient client;

    @InjectMocks
    private PostService postService;

    @Captor
    private ArgumentCaptor<Post> postCaptor;

    @BeforeEach
    public void setUp() {
        commentIds1 = new ArrayList<>();
        commentIds2 = new ArrayList<>();
        commentIds1.add(1L);
        commentIds2.add(4L);

        post1 = Post.builder()
                .id(1L)
                .title("Title 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer 1")
                .reviewReasoning("Reasoning 1")
                .commentIds(commentIds1)
                .publishedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        post2 = Post.builder()
                .id(2L)
                .title("Title 2")
                .content("Content 2")
                .author("Author 2")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer 2")
                .reviewReasoning("Reasoning 2")
                .commentIds(commentIds2)
                .publishedDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        comment1 = Comment.builder()
                .id(1L)
                .postId(1L)
                .content("Comment 1")
                .author("User 1")
                .commentDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        comment2 = Comment.builder()
                .id(4L)
                .postId(2L)
                .content("Comment 2")
                .author("User 2")
                .commentDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        response1 = PostResponse.builder()
                .id(post1.getId())
                .title(post1.getTitle())
                .content(post1.getContent())
                .author(post1.getAuthor())
                .status(post1.getStatus())
                .reviewEditor(post1.getReviewEditor())
                .reviewReasoning(post1.getReviewReasoning())
                .commentIds(post1.getCommentIds())
                .publishedDate(post1.getPublishedDate())
                .build();

        response2 = PostResponse.builder()
                .id(post2.getId())
                .title(post2.getTitle())
                .content(post2.getContent())
                .author(post2.getAuthor())
                .status(post2.getStatus())
                .reviewEditor(post2.getReviewEditor())
                .reviewReasoning(post2.getReviewReasoning())
                .commentIds(post2.getCommentIds())
                .publishedDate(post2.getPublishedDate())
                .build();

        commentResponse1 = CommentResponse.builder()
                .id(comment1.getId())
                .postId(comment1.getPostId())
                .content(comment1.getContent())
                .author(comment1.getAuthor())
                .commentDate(comment1.getCommentDate())
                .build();

        commentResponse2 = CommentResponse.builder()
                .id(comment2.getId())
                .postId(comment2.getPostId())
                .content(comment2.getContent())
                .author(comment2.getAuthor())
                .commentDate(comment2.getCommentDate())
                .build();

        request = PostRequest.builder()
                .title("Title Request")
                .content("Content Request")
                .author("Author Request")
                .status(PostStatus.PENDING)
                .reviewEditor("Reviewer Request")
                .reviewReasoning("Reasoning Request")
                .commentIds(List.of())
                .publishedDate(LocalDateTime.now().withNano(0))
                .build();

        commentRequest = CommentRequest.builder()
                .postId(1L)
                .author("User 3")
                .content("Comment 3")
                .build();
    }

    @Test
    public void testCreatePost() {
        postService.createPost(request);
        Mockito.verify(postRepository).save(postCaptor.capture());
        Post post = postCaptor.getValue();

        Assertions.assertEquals(request.getTitle(), post.getTitle());
        Assertions.assertEquals(request.getContent(), post.getContent());
        Assertions.assertEquals(request.getAuthor(), post.getAuthor());
        Assertions.assertEquals(PostStatus.PENDING, post.getStatus());
    }

    @Test
    public void testGetAllPostsWithSavedPostsShouldReturnPosts() {
        Mockito.when(postRepository.findAll()).thenReturn(List.of(post1, post2));
        Mockito.when(mapper.map(post1, PostResponse.class)).thenReturn(response1);
        Mockito.when(mapper.map(post2, PostResponse.class)).thenReturn(response2);

        List<PostResponse> postResponses = postService.getAllPosts();

        Assertions.assertEquals(2, postResponses.size());
        Assertions.assertEquals(postResponses.get(0).getId(), post1.getId());
        Assertions.assertEquals(postResponses.get(0).getTitle(), post1.getTitle());
        Assertions.assertEquals(postResponses.get(0).getContent(), post1.getContent());
        Assertions.assertEquals(postResponses.get(0).getAuthor(), post1.getAuthor());
        Assertions.assertEquals(postResponses.get(0).getStatus(), post1.getStatus());
        Assertions.assertEquals(postResponses.get(0).getReviewEditor(), post1.getReviewEditor());
        Assertions.assertEquals(postResponses.get(0).getReviewReasoning(), post1.getReviewReasoning());
        Assertions.assertEquals(postResponses.get(0).getCommentIds(), post1.getCommentIds());
        Assertions.assertEquals(postResponses.get(0).getPublishedDate(), post1.getPublishedDate());

        Assertions.assertEquals(postResponses.get(1).getId(), post2.getId());
        Assertions.assertEquals(postResponses.get(1).getTitle(), post2.getTitle());
        Assertions.assertEquals(postResponses.get(1).getContent(), post2.getContent());
        Assertions.assertEquals(postResponses.get(1).getAuthor(), post2.getAuthor());
        Assertions.assertEquals(postResponses.get(1).getStatus(), post2.getStatus());
        Assertions.assertEquals(postResponses.get(1).getReviewEditor(), post2.getReviewEditor());
        Assertions.assertEquals(postResponses.get(1).getReviewReasoning(), post2.getReviewReasoning());
        Assertions.assertEquals(postResponses.get(1).getCommentIds(), post2.getCommentIds());
        Assertions.assertEquals(postResponses.get(1).getPublishedDate(), post2.getPublishedDate());

        Mockito.verify(postRepository).findAll();
    }

    @Test
    public void testGetAllPostsWithNoneShouldThrowNotFoundException() {
        Mockito.when(postRepository.findAll()).thenReturn(List.of());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.getAllPosts());

        Mockito.verify(postRepository).findAll();
    }

    @Test
    public void testGetPostByIdAndValidIdShouldReturnPost() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        PostResponse postResponse = postService.getPostById(1L);

        Assertions.assertEquals(postResponse.getId(), post1.getId());
        Assertions.assertEquals(postResponse.getTitle(), post1.getTitle());
        Assertions.assertEquals(postResponse.getContent(), post1.getContent());
        Assertions.assertEquals(postResponse.getAuthor(), post1.getAuthor());
        Assertions.assertEquals(postResponse.getStatus(), post1.getStatus());
        Assertions.assertEquals(postResponse.getReviewEditor(), post1.getReviewEditor());
        Assertions.assertEquals(postResponse.getReviewReasoning(), post1.getReviewReasoning());
        Assertions.assertEquals(postResponse.getCommentIds(), post1.getCommentIds());
        Assertions.assertEquals(postResponse.getPublishedDate(), post1.getPublishedDate());

        Mockito.verify(postRepository).findById(1L);
    }

    @Test
    public void testGetPostByIdAndInvalidIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(3L));

        Mockito.verify(postRepository).findById(3L);
    }

    @Test
    public void testSaveEditsToPostWithValidIdShouldSavePost() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        postService.saveEditsToPost(1L, request);

        Mockito.verify(postRepository).save(postCaptor.capture());
        Post post = postCaptor.getValue();

        Assertions.assertEquals(post.getTitle(), request.getTitle());
        Assertions.assertEquals(post.getContent(), request.getContent());
        Assertions.assertEquals(post.getAuthor(), request.getAuthor());
        Assertions.assertEquals(post.getStatus(), request.getStatus());
        Assertions.assertEquals(post.getReviewEditor(), request.getReviewEditor());
        Assertions.assertEquals(post.getReviewReasoning(), request.getReviewReasoning());
        Assertions.assertEquals(post.getCommentIds(), request.getCommentIds());
        Assertions.assertEquals(post.getPublishedDate(), request.getPublishedDate());

        Mockito.verify(postRepository).findById(1L);
    }

    @Test
    public void testSaveEditsToPostWithInvalidIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> postService.saveEditsToPost(3L, request));

        Mockito.verify(postRepository).findById(3L);
    }

    @Test
    public void testAddCommentToPostWithInvalidIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.addCommentToPost(3L, commentRequest.getAuthor(), commentRequest.getContent()));

        Mockito.verify(postRepository).findById(3L);
    }

    @Test
    public void testAddCommentToPostWithValidIdShouldSaveComment() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        Mockito.when(client.createComment(commentRequest)).thenReturn(comment1);

        postService.addCommentToPost(commentRequest.getPostId(), commentRequest.getAuthor(), commentRequest.getContent());

        Mockito.verify(client).createComment(commentRequest);
        Mockito.verify(postRepository).save(postCaptor.capture());
        Post post = postCaptor.getValue();

        Assertions.assertEquals(post.getCommentIds().get(post.getCommentIds().size() - 1), comment1.getId());
    }

    @Test
    public void testGetCommentsFromPostWithInvalidIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.getCommentsForPost(3L));

        Mockito.verify(postRepository).findById(3L);
    }

    @Test
    public void testGetCommentsFromPostWithValidIdShouldReturnComments() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        Mockito.when(client.getCommentById(1L)).thenReturn(commentResponse1);

        List<CommentResponse> comments = postService.getCommentsForPost(1L);

        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(commentResponse1.getId(), comments.get(0).getId());
        Assertions.assertEquals(commentResponse1.getPostId(), comments.get(0).getPostId());
        Assertions.assertEquals(commentResponse1.getAuthor(), comments.get(0).getAuthor());
        Assertions.assertEquals(commentResponse1.getCommentDate(), comments.get(0).getCommentDate());
        Assertions.assertEquals(commentResponse1.getContent(), comments.get(0).getContent());


        Mockito.verify(postRepository).findById(1L);
    }

    @Test
    public void testUpdateCommentsFromPostWithInvalidPostIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.updateCommentForPost(3L, 1L, "update"));

        Mockito.verify(postRepository).findById(3L);
    }

    @Test
    public void testUpdateCommentsFromPostWithInvalidCommentIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        Mockito.when(client.getCommentById(4L)).thenReturn(commentResponse2);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.updateCommentForPost(1L, 4L, "Update"));

        Mockito.verify(postRepository).findById(1L);
        Mockito.verify(client).getCommentById(4L);
    }

    @Test
    public void testUpdateCommentsFromPostWithValidPostIdAndValidCommentIdShouldUpdateComment() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        Mockito.when(client.getCommentById(1L)).thenReturn(commentResponse1);

        postService.updateCommentForPost(commentResponse1.getPostId(), commentResponse1.getId(), "update");

        CommentRequest request = CommentRequest.builder()
                .postId(commentResponse1.getPostId())
                .author(commentResponse1.getAuthor())
                .content("update")
                .build();

        Mockito.verify(postRepository).findById(1L);
        Mockito.verify(client).getCommentById(1L);
        Mockito.verify(client).updateComment(1L, request);
        Mockito.verify(postRepository).save(post1);
    }

    @Test
    public void testDeleteCommentsFromPostWithInvalidPostIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.deleteCommentById(3L, 1L));

        Mockito.verify(postRepository).findById(3L);
    }

    @Test
    public void testDeleteCommentsFromPostWithInvalidCommentIdShouldThrowNotFoundException() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> postService.deleteCommentById(1L, 4L));

        Mockito.verify(postRepository).findById(1L);
    }

    @Test
    public void testDeleteCommentsFromPostWithValidIdAndValidCommentIdShouldDeleteComments() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post1));

        postService.deleteCommentById(1L, 1L);

        Mockito.verify(postRepository).findById(1L);
        Mockito.verify(client).deleteCommentById(1L);
        Mockito.verify(postRepository).save(post1);
    }

}
