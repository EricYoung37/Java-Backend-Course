package com.company.backend.mongoblog.service;

import com.company.backend.mongoblog.payload.PostDTO;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO);

    List<PostDTO> getAllPosts();

    PostDTO getPostById(String id);

    PostDTO updatePost(String id, PostDTO postDTO);

    void deletePostById(String id);
}
