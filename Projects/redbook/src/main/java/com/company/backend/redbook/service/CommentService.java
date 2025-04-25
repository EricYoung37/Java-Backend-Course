package com.company.backend.redbook.service;

import com.company.backend.redbook.payload.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long postId, CommentDTO commentDTO);

    List<CommentDTO> getCommentsByPostId(Long postId);

    CommentDTO getCommentById(Long postId, Long commentId);

    CommentDTO updateCommentById(Long id, Long commentId, CommentDTO commentDTO);

    void deleteCommentById(Long postId, Long commentId);

    List<CommentDTO> getCommentsByKeywordAfterDate(Long postId, String keyword, LocalDateTime date);
}
