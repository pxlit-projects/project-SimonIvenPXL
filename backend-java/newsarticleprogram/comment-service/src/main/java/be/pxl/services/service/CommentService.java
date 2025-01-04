package be.pxl.services.service;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repository.CommentRepository;
import be.pxl.services.service.interfaces.ICommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;

    @Override
    public Comment createComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .author(commentRequest.getAuthor())
                .content(commentRequest.getContent())
                .commentDate(LocalDateTime.now().withNano(0))
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        if (comments.isEmpty()) {
            throw new ResourceNotFoundException("No comments found just yet");
        }

        return comments.stream().map(c -> mapper.map(c, CommentResponse.class)).toList();
    }

    @Override
    public CommentResponse getCommentById(long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new ResourceNotFoundException("Comment not found");
        }

        return CommentResponse.builder()
                .id(comment.get().getId())
                .postId(comment.get().getPostId())
                .author(comment.get().getAuthor())
                .content(comment.get().getContent())
                .commentDate(comment.get().getCommentDate())
                .build();
    }
}
