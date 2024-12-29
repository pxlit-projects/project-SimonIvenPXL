package be.pxl.newsarticles.service;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.enumdata.PostStatus;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.repository.PostDraftRepository;
import be.pxl.newsarticles.repository.PostRepository;
import be.pxl.newsarticles.service.interfaces.IPostService;
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
    private final ModelMapper mapper;

    @Override
    public Post createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .status(PostStatus.PUBLISHED)
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
                .title(post.get().getTitle())
                .content(post.get().getContent())
                .author(post.get().getAuthor())
                .status(post.get().getStatus())
                .publishedDate(post.get().getPublishedDate())
                .build();
    }

}
