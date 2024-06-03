package com.cmux.postservice.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityPostDTO {

    private long communityPostid;
    private String title;
    private String content;
    private String created_Date;
    private long authorid;
    private String username;
    private long likes;
    private int commentsCount;
    private boolean is_published;
    private List<CommentDTO> comments;

    // For find teammate post
    private boolean isFindTeammatePost;
    private String instructorName;
    private String semester;
    private String courseNumber;
    private List<String> teamMembers;


    
}
