package be.pxl.services.service;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repository.CommentRepository;
import be.pxl.services.service.interfaces.ICommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);


    @Override
    public Comment createComment(CommentRequest commentRequest) {
        logger.info("Creating comment...");
        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .author(commentRequest.getAuthor())
                .content(commentRequest.getContent())
                .commentDate(LocalDateTime.now().withNano(0))
                .build();

        logger.info("Comment created");
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        logger.info("Getting all comments...");
        List<Comment> comments = commentRepository.findAll();

        if (comments.isEmpty()) {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("No comments found just yet");
        }

        logger.info("All comments found");
        return comments.stream().map(c -> mapper.map(c, CommentResponse.class)).toList();
    }

    @Override
    public CommentResponse getCommentById(long id) {
        logger.info("Getting comment by id {}", id);
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("Comment not found");
        }

        logger.info("Comment found");

        return CommentResponse.builder()
                .id(comment.get().getId())
                .postId(comment.get().getPostId())
                .author(comment.get().getAuthor())
                .content(comment.get().getContent())
                .commentDate(comment.get().getCommentDate())
                .build();
    }

    @Override
    public void deleteCommentById(long id) {
        logger.info("Deleting comment by id {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new ResourceNotFoundException("Comment not found");
        }

        logger.info("Comment deleted");
        commentRepository.delete(comment.get());
    }

    @Override
    public Comment saveEditsToComment(long id, CommentRequest commentRequest) {
        logger.info("Saving edits to comment with id {}", id);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        comment.setContent(commentRequest.getContent());
        comment.setCommentDate(LocalDateTime.now().withNano(0));

        logger.info("Comment saved");
        return commentRepository.save(comment);
    }
}
