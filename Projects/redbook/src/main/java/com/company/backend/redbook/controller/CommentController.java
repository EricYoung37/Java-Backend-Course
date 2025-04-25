package com.company.backend.redbook.controller;

import com.company.backend.redbook.payload.CommentDTO;
import com.company.backend.redbook.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1") // note we don't have /comments here
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    ResponseEntity<CommentDTO> createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    ResponseEntity<CommentDTO> getCommentById(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    ResponseEntity<CommentDTO> updateCommentById(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId,
            @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateCommentById(postId, commentId, commentDTO));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    ResponseEntity<String> deleteCommentById(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("Deleted comment with ID: " + commentId, HttpStatus.OK);
    }

    // GET /api/posts/3/comments/search?keyword=hello&date=2024-11-01T00:00:00
    @GetMapping("posts/{postId}/comments/search")
    public ResponseEntity<List<CommentDTO>> getCommentsByKeywordAfterDate(
            @PathVariable Long postId,
            @RequestParam String keyword,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        List<CommentDTO> comments = commentService.getCommentsByKeywordAfterDate(postId, keyword, date);
        return ResponseEntity.ok(comments);
    }
}
