package com.cmux.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmux.postservice.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    
}
