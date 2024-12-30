package be.pxl.newsarticles.service.interfaces;

import be.pxl.newsarticles.domain.Post;
import be.pxl.newsarticles.dto.post.PostRequest;
import be.pxl.newsarticles.dto.post.PostResponse;

import java.util.List;

public interface IPostService {

    Post createPost(PostRequest postRequest);
    List<PostResponse> getAllPosts();
    PostResponse getPostById(long id);
    Post saveEditsToPost(long id, PostRequest postRequest);

}
