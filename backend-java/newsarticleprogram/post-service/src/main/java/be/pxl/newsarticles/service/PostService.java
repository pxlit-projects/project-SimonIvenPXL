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

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostDraftRepository postDraftRepository;
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
    public Post savePostAsDraft(PostRequest postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .status(PostStatus.DRAFT)
                .publishedDate(LocalDateTime.now().withNano(0))
                .build();

        return postDraftRepository.save(post);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            return new ArrayList<>();
        }

        return posts.stream().map(p -> mapper.map(p, PostResponse.class)).toList();
    }
}
