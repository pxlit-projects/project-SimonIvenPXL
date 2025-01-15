package be.pxl.services.service;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    private Comment comment1;
    private Comment comment2;

    private CommentResponse response1;
    private CommentResponse response2;

    private CommentRequest request;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private CommentService commentService;

    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    @BeforeEach
    public void setUp() {
        comment1 = Comment.builder()
                .id(1L)
                .postId(1L)
                .content("Reasoning 1")
                .author("Author 1")
                .commentDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        comment2 = Comment.builder()
                .id(1L)
                .postId(2L)
                .content("Reasoning 2")
                .author("Author 2")
                .commentDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();


        response1 = CommentResponse.builder()
                .id(comment1.getId())
                .postId(comment1.getPostId())
                .content(comment1.getContent())
                .author(comment1.getAuthor())
                .commentDate(comment1.getCommentDate())
                .build();

        response2 = CommentResponse.builder()
                .id(comment2.getId())
                .postId(comment2.getPostId())
                .content(comment2.getContent())
                .author(comment2.getAuthor())
                .commentDate(comment2.getCommentDate())
                .build();

        request = CommentRequest.builder()
                .postId(1L)
                .content("Reasoning request")
                .author("Author request")
                .build();
    }

    @Test
    public void testCreateComment() {
        commentService.createComment(request);
        Mockito.verify(commentRepository).save(commentCaptor.capture());
        Comment comment = commentCaptor.getValue();

        Assertions.assertEquals(request.getPostId(), comment.getPostId());
        Assertions.assertEquals(request.getContent(), comment.getContent());
        Assertions.assertEquals(request.getAuthor(), comment.getAuthor());
    }

    @Test
    public void testGetAllCommentsWithSavedCommentsShouldReturnComments() {
        Mockito.when(commentRepository.findAll()).thenReturn(List.of(comment1, comment2));
        Mockito.when(mapper.map(comment1, CommentResponse.class)).thenReturn(response1);
        Mockito.when(mapper.map(comment2, CommentResponse.class)).thenReturn(response2);

        List<CommentResponse> commentResponses = commentService.getAllComments();

        Assertions.assertEquals(2, commentResponses.size());
        Assertions.assertEquals(commentResponses.get(0).getId(), comment1.getId());
        Assertions.assertEquals(commentResponses.get(0).getPostId(), comment1.getPostId());
        Assertions.assertEquals(commentResponses.get(0).getContent(), comment1.getContent());
        Assertions.assertEquals(commentResponses.get(0).getAuthor(), comment1.getAuthor());
        Assertions.assertEquals(commentResponses.get(0).getCommentDate(), comment1.getCommentDate());

        Assertions.assertEquals(commentResponses.get(1).getId(), comment2.getId());
        Assertions.assertEquals(commentResponses.get(1).getPostId(), comment2.getPostId());
        Assertions.assertEquals(commentResponses.get(1).getContent(), comment2.getContent());
        Assertions.assertEquals(commentResponses.get(1).getAuthor(), comment2.getAuthor());
        Assertions.assertEquals(commentResponses.get(1).getCommentDate(), comment2.getCommentDate());

        Mockito.verify(commentRepository).findAll();
    }

    @Test
    public void testGetAllCommentsWithNoneShouldThrowNotFoundException() {
        Mockito.when(commentRepository.findAll()).thenReturn(List.of());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.getAllComments());

        Mockito.verify(commentRepository).findAll();
    }

    @Test
    public void testGetCommentByIdAndValidIdShouldReturnComment() {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));

        CommentResponse commentResponse = commentService.getCommentById(1L);

        Assertions.assertEquals(commentResponse.getId(), comment1.getId());
        Assertions.assertEquals(commentResponse.getPostId(), comment1.getPostId());
        Assertions.assertEquals(commentResponse.getContent(), comment1.getContent());
        Assertions.assertEquals(commentResponse.getAuthor(), comment1.getAuthor());
        Assertions.assertEquals(commentResponse.getCommentDate(), comment1.getCommentDate());

        Mockito.verify(commentRepository).findById(1L);
    }

    @Test
    public void testGetCommentByIdAndInvalidIdShouldThrowNotFoundException() {
        Mockito.when(commentRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(3L));

        Mockito.verify(commentRepository).findById(3L);
    }
}
