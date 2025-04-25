package com.company.backend.redbook.service;

import com.company.backend.redbook.payload.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostDTO getPostById(Long id);

    List<PostDTO> getAllPosts();

    PostDTO updatePost(Long id, PostDTO postDTO);

    void deletePostById(Long id);
}
