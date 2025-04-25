package com.company.backend.redbook.dao;

import com.company.backend.redbook.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    // Find comments based on content containing a keyword, created after a certain date, and ordered by createdDateTime
    List<Comment> findByContentContainingAndCreatedDateTimeAfterOrderByCreatedDateTimeDesc(
            String keyword,
            LocalDateTime date
    );
}
