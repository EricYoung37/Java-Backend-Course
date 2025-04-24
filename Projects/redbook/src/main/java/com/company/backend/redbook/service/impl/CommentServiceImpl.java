package com.company.backend.redbook.service.impl;

import com.company.backend.redbook.dao.CommentRepository;
import com.company.backend.redbook.dao.PostRepository;
import com.company.backend.redbook.entity.Comment;
import com.company.backend.redbook.entity.Post;
import com.company.backend.redbook.exception.BadResourceRequestException;
import com.company.backend.redbook.exception.ResourceNotFoundException;
import com.company.backend.redbook.payload.CommentDTO;
import com.company.backend.redbook.service.CommentService;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(ModelMapper modelMapper, PostRepository postRepository, CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        // find post first
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        comment.setPost(post);
        return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        Comment comment = matchCommentByPost(postId, commentId);

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO updateCommentById(Long postId, Long commentId, CommentDTO commentDTO) {
        Comment comment = matchCommentByPost(postId, commentId);

        comment.setContent(commentDTO.getContent());
        return modelMapper.map(commentRepository.save(comment), CommentDTO.class);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Comment comment = matchCommentByPost(postId, commentId);

        commentRepository.delete(comment);
    }

    private Comment matchCommentByPost(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BadResourceRequestException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return comment;
    }
}
