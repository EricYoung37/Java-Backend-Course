package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post post;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setName("John Doe");
        comment.setEmail("john.doe@example.com");
        comment.setBody("This is a comment");
        comment.setPost(post);

        commentDto = new CommentDto(1L, "John Doe", "john.doe@example.com", "This is a comment");
    }

    @Test
    void createComment_ShouldReturnCommentDto() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        when(modelMapper.map(commentDto, Comment.class)).thenReturn(comment);
        when(modelMapper.map(comment, CommentDto.class)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(post.getId(), commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getId(), result.getId());
        assertEquals(commentDto.getName(), result.getName());

        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(modelMapper, times(1)).map(commentDto, Comment.class);
        verify(modelMapper, times(1)).map(comment, CommentDto.class);
    }


    @Test
    void createComment_PostNotFound_ShouldThrowResourceNotFoundException() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(post.getId(), commentDto));
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void getCommentsByPostId_ShouldReturnCommentDtoList() {
        List<Comment> comments = Arrays.asList(comment);
        List<CommentDto> commentDtos = Arrays.asList(commentDto);

        when(commentRepository.findByPostId(post.getId())).thenReturn(comments);
        when(modelMapper.map(any(Comment.class), eq(CommentDto.class))).thenReturn(commentDto);

        List<CommentDto> result = commentService.getCommentsByPostId(post.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(commentDto.getId(), result.get(0).getId());
        verify(commentRepository, times(1)).findByPostId(post.getId());
    }

    @Test
    void getCommentById_ShouldReturnCommentDto() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(modelMapper.map(any(Comment.class), eq(CommentDto.class))).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(post.getId(), comment.getId());

        assertNotNull(result);
        assertEquals(commentDto.getId(), result.getId());
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
    }

    @Test
    void getCommentById_PostNotFound_ShouldThrowResourceNotFoundException() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(post.getId(), comment.getId()));
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(0)).findById(comment.getId());
    }

    @Test
    void getCommentById_CommentNotFound_ShouldThrowResourceNotFoundException() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(post.getId(), comment.getId()));
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
    }

    @Test
    void getCommentById_CommentDoesNotBelongToPost_ShouldThrowBlogAPIException() {
        Post differentPost = new Post();
        differentPost.setId(2L);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        comment.setPost(differentPost);

        assertThrows(BlogAPIException.class, () -> commentService.getCommentById(post.getId(), comment.getId()));
    }

    @Test
    void updateComment_ShouldReturnUpdatedCommentDto() {
        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setName("Updated Name");
        updatedComment.setEmail("updated.email@example.com");
        updatedComment.setBody("Updated comment body");

        CommentDto updatedCommentDto = new CommentDto(1L, "Updated Name", "updated.email@example.com", "Updated comment body");

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);
        when(modelMapper.map(any(Comment.class), eq(CommentDto.class))).thenReturn(updatedCommentDto);

        CommentDto result = commentService.updateComment(post.getId(), comment.getId(), updatedCommentDto);

        assertNotNull(result);
        assertEquals(updatedCommentDto.getId(), result.getId());
        assertEquals(updatedCommentDto.getName(), result.getName());
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateComment_PostNotFound_ShouldThrowResourceNotFoundException() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.updateComment(post.getId(), comment.getId(), commentDto));
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(0)).findById(comment.getId());
    }

    @Test
    void updateComment_ShouldThrowException_WhenCommentDoesNotBelongToPost() {
        Post anotherPost = new Post();
        anotherPost.setId(99L); // Different from postId (1L)

        Comment commentWithDifferentPost = new Comment();
        commentWithDifferentPost.setId(1L);
        commentWithDifferentPost.setPost(anotherPost);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentWithDifferentPost));

        BlogAPIException ex = assertThrows(
                BlogAPIException.class,
                () -> commentService.updateComment(1L, 1L, commentDto)
        );

        assertEquals("Comment does not belong to post", ex.getMessage());

        verify(postRepository).findById(1L);
        verify(commentRepository).findById(1L);
    }

    @Test
    void deleteComment_ShouldDeleteComment() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.deleteComment(post.getId(), comment.getId());

        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_PostNotFound_ShouldThrowResourceNotFoundException() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(post.getId(), comment.getId()));
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(0)).findById(comment.getId());
    }

    @Test
    void deleteComment_CommentNotFound_ShouldThrowResourceNotFoundException() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(post.getId(), comment.getId()));
        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
    }

    @Test
    void deleteComment_CommentDoesNotBelongToPost_ShouldThrowBlogAPIException() {
        Post differentPost = new Post();
        differentPost.setId(2L);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        comment.setPost(differentPost);

        assertThrows(BlogAPIException.class, () -> commentService.deleteComment(post.getId(), comment.getId()));
    }
}
