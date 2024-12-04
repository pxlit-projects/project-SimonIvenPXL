package be.pxl.newsarticles.service;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;
import be.pxl.newsarticles.exception.ResourceNotFoundException;
import be.pxl.newsarticles.repository.PostRepository;
import be.pxl.newsarticles.service.interfaces.IPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;

    @Override
    public void createPost(PostRequest postRequest) {

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
