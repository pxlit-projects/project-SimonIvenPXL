package be.pxl.services.service.interfaces;

import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;

import java.util.List;

public interface ICommentService {
    Comment createComment(CommentRequest commentRequest);
    List<CommentResponse> getAllComments();
    CommentResponse getCommentById(long id);
}
