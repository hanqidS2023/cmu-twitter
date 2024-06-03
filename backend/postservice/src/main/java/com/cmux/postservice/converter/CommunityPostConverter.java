package com.cmux.postservice.converter;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.cmux.postservice.dto.CommentDTO;
import org.springframework.stereotype.Component;
import com.cmux.postservice.dto.CommunityPostDTO;
import com.cmux.postservice.model.Comment;
import com.cmux.postservice.model.CommunityPost;

@Component
public class CommunityPostConverter {

    public CommunityPostDTO convertToDTO(CommunityPost communityPost) {
        CommentConverter commentConverter = new CommentConverter();
        CommunityPostDTO dto = new CommunityPostDTO();
        dto.setCommunityPostid(communityPost.getCommunityPostid());
        dto.setTitle(communityPost.getTitle());
        dto.setContent(communityPost.getContent());
        dto.setCreated_Date(communityPost.getCreated_Date());
        dto.setAuthorid(communityPost.getAuthorid());
        dto.setLikes(communityPost.getLikes());
        dto.set_published(communityPost.is_published());
        dto.setUsername(communityPost.getUsername());
        if (communityPost.getComments() != null) {
            ArrayList<CommentDTO> commentList = new ArrayList<CommentDTO>();
            for (int i = 0; i < communityPost.getComments().size(); i++) {
                commentList.add(commentConverter.convertEntityToDTO(communityPost.getComments().get(i)));
            }
            dto.setComments(commentList);
            dto.setCommentsCount(communityPost.getComments().size());
            // dto.setComments(communityPost.getComments().stream()
            // .map(commentService.convertEntityToDTO)
            // .collect(Collectors.toList()));
        } else {
            dto.setComments(new ArrayList<>());
            dto.setCommentsCount(0);
        }

        dto.setFindTeammatePost(communityPost.isFindTeammatePost());
        dto.setInstructorName(communityPost.getInstructorName());
        dto.setCourseNumber(communityPost.getCourseNumber());
        dto.setSemester(communityPost.getSemester());
        if (communityPost.getTeamMembers() != null && !communityPost.getTeamMembers().isEmpty()) {
            dto.setTeamMembers(communityPost.getTeamMembers());
        } else {
            dto.setTeamMembers(new ArrayList<>());
        }
        

        return dto;
    }

    public CommunityPost convertToEntity(CommunityPostDTO communityPostDTO) {
        CommunityPost communityPost = new CommunityPost();
        CommentConverter commentConverter = new CommentConverter();
        communityPost.setTitle(communityPostDTO.getTitle());
        communityPost.setContent(communityPostDTO.getContent());
        communityPost.setCreated_Date(communityPostDTO.getCreated_Date());

        communityPost.setAuthorid(communityPostDTO.getAuthorid());
        communityPost.setLikes(communityPostDTO.getLikes());
        communityPost.setCommentsCount(communityPostDTO.getCommentsCount());
        communityPost.set_published(communityPostDTO.is_published());
        communityPost.setFindTeammatePost(communityPostDTO.isFindTeammatePost());
        communityPost.setInstructorName(communityPostDTO.getInstructorName());
        communityPost.setCourseNumber(communityPostDTO.getCourseNumber());
        communityPost.setSemester(communityPostDTO.getSemester());
        communityPost.setUsername(communityPostDTO.getUsername());
        if (communityPostDTO.getTeamMembers() != null && !communityPostDTO.getTeamMembers().isEmpty()) {
            // Split the String into a List<String>
            communityPost.setTeamMembers(communityPostDTO.getTeamMembers());
        } else {
            communityPost.setTeamMembers(new ArrayList<>());
        }

        // set comments
        if (communityPostDTO.getComments() != null) {
            ArrayList<Comment> commentList = new ArrayList<Comment>();
            for (int i = 0; i < communityPostDTO.getComments().size(); i++) {
                commentList.add(commentConverter.convertDTOToEntity(communityPostDTO.getComments().get(i)));
            }    
            communityPost.setComments(commentList);
        } else {
            communityPost.setComments(null);
        }
        
        return communityPost;
    }

    public CommunityPost updateEntityWithDTO(CommunityPost existingPost, CommunityPostDTO updatePostDTO) {
        CommunityPost updatePost = this.convertToEntity(updatePostDTO);

        existingPost.setTitle(updatePost.getTitle());
        existingPost.setContent(updatePost.getContent());
        existingPost.setCreated_Date(updatePost.getCreated_Date());
        existingPost.setAuthorid(updatePost.getAuthorid());
        existingPost.setLikes(updatePost.getLikes());
        existingPost.setCommentsCount(updatePost.getCommentsCount());
        existingPost.set_published(updatePost.is_published());
        existingPost.setUsername(updatePost.getUsername());
        // set comments
        if (updatePost.getComments() != null) {
            ArrayList<Comment> commentList = new ArrayList<Comment>();
            for (int i = 0; i < updatePost.getComments().size(); i++) {
                commentList.add(updatePost.getComments().get(i));
            }
            existingPost.setComments(commentList);
        }

        existingPost.setFindTeammatePost(updatePost.isFindTeammatePost());
        existingPost.setInstructorName(updatePost.getInstructorName());
        existingPost.setCourseNumber(updatePost.getCourseNumber());
        existingPost.setSemester(updatePost.getSemester());
        existingPost.setTeamMembers(updatePost.getTeamMembers());

        return existingPost;
    }
}
