package com.cmux.postservice.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.cmux.postservice.converter.CommentConverter;
import com.cmux.postservice.model.CommentEvents;
import com.cmux.postservice.dto.CommentDTO;
import com.cmux.postservice.service.CommentService;

@Component
public class CommentEventListener {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CommentConverter commentConverter;
    private final String index = "comment";


    @EventListener
    public void onCommentCreated(CommentEvents.Created event) {
        String id = String.valueOf(event.getComment().getCommentid());

        commentService.index(this.index, id, event.getComment());

        CommentDTO dto = commentConverter.convertEntityToDTO(event.getComment());
        
        messagingTemplate.convertAndSend("/topic/comment-create", dto);
    }

    @EventListener
    public void onCommentDeleted(CommentEvents.Deleted event) {
        String id = String.valueOf(event.getCommentId());

        commentService.deleteIndex(this.index, id);

        System.out.println("Comment deleted");
    }

    @EventListener
    public void onCommentUpdated(CommentEvents.Updated event) {
        String id = String.valueOf(event.getComment().getCommentid());

        commentService.index(this.index, id, event.getComment());

        System.out.println("Comment updated");
    }

}
