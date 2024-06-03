package com.cmux.postservice.service;

import org.springframework.stereotype.Service;
import com.cmux.postservice.model.Comment;
import com.cmux.postservice.dto.CommentDTO;
import com.cmux.postservice.model.PostEvents;
import com.cmux.postservice.model.CommentEvents;
import java.util.NoSuchElementException;
import com.cmux.postservice.converter.CommentConverter;
import com.cmux.postservice.repository.CommentRepository;
import org.springframework.context.ApplicationEventPublisher;
import jakarta.transaction.Transactional;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import java.util.Optional;

@Service
public class CommentService extends AbstractESService<Comment> {

    private CommentRepository commentRepository;
    private CommentConverter commentConverter;
    private final ApplicationEventPublisher publisher;

    public CommentService(CommentRepository commentRepository,
            CommentConverter commentConverter,
            ApplicationEventPublisher publisher, ElasticsearchClient elasticsearchClient) {
        super(elasticsearchClient);
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.publisher = publisher;

    }

    public CommentDTO saveComment(CommentDTO commentdto) {
        Comment comment = commentConverter.convertDTOToEntity(commentdto);
        System.out.println("CommentService: saveComment, comment: " + comment.getCommunityPost().getCommunityPostid());
        // System.out.println("CommentService: saveComment, communitypostId: " + comment.getCommunityPost().getCommunityPostid);
        commentRepository.save(comment);

        this.publisher.publishEvent(new CommentEvents.Created(comment));

        return commentConverter.convertEntityToDTO(comment);
    }

    @Transactional
    public Optional<CommentDTO> getCommentById(long commentid) {
        Optional<Comment> comment = commentRepository.findById(commentid);
        if (comment.isPresent()) {
            CommentDTO commentdto = commentConverter.convertEntityToDTO(comment.get());
            return Optional.of(commentdto);
        } else {
            throw new NoSuchElementException("Comment not found for id: " + commentid);
        }
    }

    public void deleteCommentById(long commentid) {
        Optional<Comment> comment = commentRepository.findById(commentid);
        if (comment.isPresent()) {
            commentRepository.deleteById(commentid);
            this.publisher.publishEvent(new CommentEvents.Deleted(commentid));
            System.out.println("CommentService: deleteCommentById: deleted comment with id " + commentid);
        } else {
            throw new NoSuchElementException("Comment not found for id: " + commentid);
        }
    }

    @Transactional
    public CommentDTO updateComment(long commentid, CommentDTO commentdto) {
        Comment existComment = commentRepository.findById(commentid)
                .orElseThrow(() -> new NoSuchElementException());

        existComment = commentConverter.updateEntityWithDTO(existComment, commentdto);

        Comment updateComment = commentRepository.save(existComment);

        return commentConverter.convertEntityToDTO(updateComment);
    }

    @Override
    public void index(String index, String id, Comment comment) {
        super.index(index, id, comment);

        System.out.println("CommentService: index: indexed comment with id " + id);
    }

    @Override
    public void deleteIndex(String index, String id) {
        super.deleteIndex(index, id);

        System.out.println("CommentService: deleteIndex: deleted comment with id " + id);
    }

}
