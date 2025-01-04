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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @Override
    public Post createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .status(PostStatus.PENDING)
                .publishedDate(LocalDateTime.now().withNano(0))
                .build();

        return postRepository.save(post);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            throw new ResourceNotFoundException("Er zijn nog geen posts aangemaakt");
        }

        return posts.stream().map(p -> mapper.map(p, PostResponse.class)).toList();
    }

    @Override
    public PostResponse getPostById(long id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("No post found with ID " + id);
        }

        return PostResponse.builder()
                .id(post.get().getId())
                .title(post.get().getTitle())
                .content(post.get().getContent())
                .author(post.get().getAuthor())
                .status(post.get().getStatus())
                .publishedDate(post.get().getPublishedDate())
                .build();
    }

    @Override
    public Post saveEditsToPost(long id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + id));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(postRequest.getAuthor());
        post.setStatus(postRequest.getStatus());
        post.setReviewEditor(postRequest.getReviewEditor());
        post.setReviewReasoning(postRequest.getReviewReasoning());
        post.setPublishedDate(LocalDateTime.now().withNano(0));

        return postRepository.save(post);
    }

    @Override
    public Post addCommentToPost(long postId, String author, String comment) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("No post found with ID " + postId));

        CommentRequest commentRequest = CommentRequest.builder()
                .postId(postId)
                .author(author)
                .content(comment)
                .build();

        Comment createdComment = commentClient.createComment(commentRequest);

        post.getCommentIds().add(createdComment.getId());

        return postRepository.save(post);
    }

}
