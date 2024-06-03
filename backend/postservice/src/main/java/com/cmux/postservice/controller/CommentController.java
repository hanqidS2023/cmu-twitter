package com.cmux.postservice.controller;

import com.cmux.postservice.dto.CommentDTO;
import com.cmux.postservice.service.CommentService;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/comments")

public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public CommentDTO savComment(@RequestBody CommentDTO commentdto) {
        String dateNow = java.time.LocalDate.now().toString();
        commentdto.setCreated_Date(dateNow);
        return commentService.saveComment(commentdto);
    }

    @GetMapping("/{commentid}")
    public CommentDTO getComment(@PathVariable long commentid) {
        return commentService.getCommentById(commentid)
                .orElseThrow(() -> new NoSuchElementException("comment not found with id: " + commentid));
    }

    @DeleteMapping("/{commentid}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentid) {
        try {
            commentService.deleteCommentById(commentid);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting comment");
        }
    }

    @PutMapping("/{commentid}")
    public ResponseEntity<?> updateComment(@PathVariable long commentid, @RequestBody CommentDTO commentdto) {
        try {
            CommentDTO updatedComment = commentService.updateComment(commentid, commentdto);
            return ResponseEntity.ok(updatedComment);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error updating comment");
        }
    }
}
