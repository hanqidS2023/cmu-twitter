package com.cmux.postservice.model;

import lombok.Data;

// Post Events
public class PostEvents {
    
    @Data
    public static class Created {
        private final CommunityPost communityPost;
        public Created(CommunityPost communityPost) {
            this.communityPost = communityPost;
        }
    }
    @Data
    public static class Updated {
        private final CommunityPost communityPost;
        public Updated(CommunityPost communityPost) {
            this.communityPost = communityPost;
        }
    }
    @Data
    public static class Deleted {
        private final long postId;
        public Deleted(long postId) {
            this.postId = postId;
        }
    }
}

