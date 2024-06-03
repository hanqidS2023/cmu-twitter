package com.cmux.postservice.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.cmux.postservice.controller.MQProducer;
import com.cmux.postservice.dto.NewCreditMessage;
import com.cmux.postservice.service.CommentService;
import com.cmux.postservice.service.CommunityPostService;
import com.cmux.postservice.model.PostEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PostEventListener {

    private final CommunityPostService communityPostService;
    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MQProducer messageProducer;
    private final String id = "communitypost";
    private final ObjectMapper mapper = new ObjectMapper();

    public PostEventListener(CommunityPostService communityPostService, CommentService commentService, SimpMessagingTemplate messagingTemplate, MQProducer messageProducer) {
        this.communityPostService = communityPostService;
        this.commentService = commentService;
        this.messagingTemplate = messagingTemplate;
        this.messageProducer = messageProducer;
    }

    @EventListener
    public void onPostCreated(PostEvents.Created event) {
        String postID = String.valueOf(event.getCommunityPost().getCommunityPostid());

        communityPostService.index(this.id, postID, event.getCommunityPost());

        messagingTemplate.convertAndSend("/topic/post-created", event.getCommunityPost());
    }


    @EventListener
    public void onPostUpdated(PostEvents.Updated event) throws JsonProcessingException {
        String postID = String.valueOf(event.getCommunityPost().getCommunityPostid());

        communityPostService.index(this.id, String.valueOf(event.getCommunityPost().getCommunityPostid()),
                event.getCommunityPost());
        final long authorid = event.getCommunityPost().getAuthorid();
        final int credit_amount = 10;
        NewCreditMessage purchaseProductMessage = new NewCreditMessage(authorid, credit_amount);
        String jsonString = mapper.writeValueAsString(purchaseProductMessage);
        messageProducer.sendIdToReward(jsonString);
        messagingTemplate.convertAndSend("/topic/post-update", event.getCommunityPost());
    }

    @EventListener
    public void onPostDeleted(PostEvents.Deleted event) {
        String postID = String.valueOf(event.getPostId());

        commentService.deleteIndex(this.id, postID);

        System.out.println("PostEventListener: onPostDeleted: deleted document with id " + postID);
    
        messagingTemplate.convertAndSend("/topic/post-delete", event.getPostId());
    }
}
