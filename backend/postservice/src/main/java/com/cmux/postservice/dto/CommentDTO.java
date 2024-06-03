package com.cmux.postservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class CommentDTO {
    
    private long commentid;
    private String content;
    private String created_Date;
    private long authorid;
    private String username;
    private long likes;
    private long communityPostid;

    
}
