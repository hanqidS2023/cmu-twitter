package com.cmux.postservice.model;

import lombok.Data;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;


@Data
@Entity
@Table(name = "comment")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentid;

    private String content;
    private String created_Date;
    private long authorid;
    private String username;
    private long likes;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "communityPostid")
    @JsonBackReference 
    private CommunityPost communityPost;
}
