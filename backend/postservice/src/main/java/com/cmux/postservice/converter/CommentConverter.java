package com.cmux.postservice.converter;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmux.postservice.dto.CommentDTO;
import org.springframework.stereotype.Component;
import com.cmux.postservice.model.Comment;
import com.cmux.postservice.model.CommunityPost;
import com.cmux.postservice.repository.CommunityPostRepository;

@Component
public class CommentConverter {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    public Comment convertDTOToEntity(CommentDTO commentdto) {
        Comment comment = new Comment();
        // CommunityPostDTO communityPostDTO = new CommunityPostDTO();
        comment.setCommentid(commentdto.getCommentid());
        comment.setContent(commentdto.getContent());
        comment.setCreated_Date(commentdto.getCreated_Date());
        comment.setAuthorid(commentdto.getAuthorid());
        comment.setLikes(commentdto.getLikes());
        comment.setUsername(commentdto.getUsername());
        Optional<CommunityPost> communityPost = communityPostRepository.findById(commentdto.getCommunityPostid());
        if (communityPost.isPresent()) {
            comment.setCommunityPost(communityPost.get());
            System.out.println("CommentConverter: convertDTOToEntity, communityPostid: " + comment.getCommunityPost().getCommunityPostid());
        } else {
            throw new NoSuchElementException("Community post not found for this id");
        }

        return comment;
    }

    public CommentDTO convertEntityToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentid(comment.getCommentid());
        dto.setContent(comment.getContent());
        dto.setCreated_Date(comment.getCreated_Date());
        dto.setAuthorid(comment.getAuthorid());
        dto.setLikes(comment.getLikes());
        dto.setUsername(comment.getUsername());
        dto.setCommunityPostid(comment.getCommunityPost().getCommunityPostid());
        return dto;
    }

    public Comment updateEntityWithDTO(Comment existingComment, CommentDTO commentDTO) {
        Comment comment = this.convertDTOToEntity(commentDTO);
        existingComment.setContent(comment.getContent());
        existingComment.setCreated_Date(comment.getCreated_Date());
        existingComment.setAuthorid(comment.getAuthorid());
        existingComment.setLikes(comment.getLikes());
        existingComment.setUsername(comment.getUsername());
        CommunityPost communityPost = comment.getCommunityPost();
        if (communityPost != null) {
            existingComment.setCommunityPost(communityPost);
        } else {
            throw new NoSuchElementException("Community post not found for this id");
        }
        return existingComment;
    }
}
