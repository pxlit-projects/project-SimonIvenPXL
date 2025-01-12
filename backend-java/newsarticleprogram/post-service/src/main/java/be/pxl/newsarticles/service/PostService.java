package be.pxl.newsarticles.service;

import be.pxl.newsarticles.client.CommentClient;
import be.pxl.newsarticles.domain.Draft;
import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.draft.DraftRequest;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.repository.PostDraftRepository;
import be.pxl.newsarticles.repository.PostRepository;
import be.pxl.newsarticles.service.interfaces.IPostService;
import be.pxl.services.domain.Comment;
import be.pxl.services.dto.CommentRequest;
import be.pxl.services.dto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final CommentClient commentClient;
    private final ModelMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    @Override
    public Post createPost(PostRequest postRequest) {
        logger.info("Creating new post");
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .status(PostStatus.PENDING)
                .publishedDate(LocalDateTime.now().withNano(0))
                .build();

        logger.info("Post created");
        return postRepository.save(post);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        logger.info("Getting all the posts");
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("Er zijn nog geen posts aangemaakt");
        }

        logger.info("Posts found");
        return posts.stream().map(p -> mapper.map(p, PostResponse.class)).toList();
    }

    @Override
    public PostResponse getPostById(long id) {
        logger.info("Getting post by id");
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("No post found with ID " + id);
        }

        logger.info("Post found");
        return PostResponse.builder()
                .id(post.get().getId())
                .title(post.get().getTitle())
                .content(post.get().getContent())
                .author(post.get().getAuthor())
                .status(post.get().getStatus())
                .reviewEditor(post.get().getReviewEditor())
                .reviewReasoning(post.get().getReviewReasoning())
                .commentIds(post.get().getCommentIds())
                .publishedDate(post.get().getPublishedDate())
                .build();
    }

    @Override
    public Post saveEditsToPost(long id, PostRequest postRequest) {
        logger.info("Saving edits to post");
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + id));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(postRequest.getAuthor());
        post.setStatus(postRequest.getStatus());
        post.setReviewEditor(postRequest.getReviewEditor());
        post.setReviewReasoning(postRequest.getReviewReasoning());
        post.setPublishedDate(LocalDateTime.now().withNano(0));
        post.setCommentIds(postRequest.getCommentIds());

        logger.info("Post saved");
        return postRepository.save(post);
    }

    @Override
    public Post addCommentToPost(long postId, String author, String comment) {
        logger.info("Adding comment to post");
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + postId));

        CommentRequest commentRequest = CommentRequest.builder()
                .postId(postId)
                .author(author)
                .content(comment)
                .build();

        Comment createdComment = commentClient.createComment(commentRequest);

        post.getCommentIds().add(createdComment.getId());

        logger.info("Comment added");
        return postRepository.save(post);
    }

    @Override
    public List<CommentResponse> getCommentsForPost(long postId) {
        logger.info("Getting comments for post");
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + postId));

        List<CommentResponse> comments = new ArrayList<>();

        for (Long commentId : post.getCommentIds()) {
            try {
                CommentResponse comment = commentClient.getCommentById(commentId);
                comments.add(comment);
            } catch (Exception e) {
                System.err.println("Failed to fetch comment with ID: " + commentId);
            }
        }

        logger.info("Comments found");
        return comments;

    }

    @Override
    public Post updateCommentForPost(long postId, long commentId, String comment) {
        logger.info("Updating comment to post");
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + postId));

        List<Long> commentIds = post.getCommentIds();
        CommentResponse commentResponse = commentClient.getCommentById(commentId);

        if (!commentIds.contains(commentId) || postId != commentResponse.getPostId()) {
            throw new ResourceNotFoundException("No comment found with ID " + commentId);
        }

        CommentRequest commentRequest = CommentRequest.builder()
                .postId(commentResponse.getPostId())
                .author(commentResponse.getAuthor())
                .content(comment)
                .build();

        commentClient.updateComment(commentId, commentRequest);


        logger.info("Comment updated");
        return postRepository.save(post);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        logger.info("Deleting comment to post");
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + postId));


        if (post.getCommentIds().contains(commentId)) {
            commentClient.deleteCommentById(commentId);
            post.getCommentIds().remove(commentId);
            postRepository.save(post);
            logger.info("Comment deleted");
        } else {
            logger.info("Something went wrong!");
            throw new ResourceNotFoundException("No comment found with ID " + commentId);
        }
    }

}
