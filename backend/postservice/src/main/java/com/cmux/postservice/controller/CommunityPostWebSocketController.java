package com.cmux.postservice.controller;

import com.cmux.postservice.model.CommunityPost;
import com.cmux.postservice.service.CommunityPostService;
import com.cmux.postservice.converter.CommunityPostConverter;
import com.cmux.postservice.dto.CommunityPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CommunityPostWebSocketController {

    private CommunityPostService communityPostService;
    private SimpMessagingTemplate messagingTemplate;
    private CommunityPostConverter communityPostConverter;

    @Autowired
    public CommunityPostWebSocketController(CommunityPostService communityPostService, SimpMessagingTemplate messagingTemplate, CommunityPostConverter communityPostConverter) {
        this.communityPostService = communityPostService;
        this.messagingTemplate = messagingTemplate;
        this.communityPostConverter = communityPostConverter;
    }

    // @MessageMapping("/communityPost.createPost")
    // public void createPost(@Payload CommunityPost post) {
    //     // Process the creation of a new post
    //     CommunityPostDTO postDTO = communityPostConverter.convertToDTO(post);
    //     System.out.println("Received new post from frontend: " + postDTO);
    //     CommunityPostDTO newPostDTO = communityPostService.savePost(postDTO);
    //     // Broadcast the new post to subscribers
    //     messagingTemplate.convertAndSend("/topic/communityPostCreate", newPostDTO);
    // }

    @MessageMapping("/communityPost.addLike")
    public void addLike(@Payload Long postId) {
        // Process adding a like to a post
        CommunityPost updatedPost = communityPostService.addLikeToPost(postId);
        messagingTemplate.convertAndSend("/topic/communityPost/" + postId, updatedPost);
    }
}
