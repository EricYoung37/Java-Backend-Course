package com.company.backend.redbook.payload;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String content, LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        this.id = id;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", updatedDateTime=" + updatedDateTime +
                '}';
    }
}
