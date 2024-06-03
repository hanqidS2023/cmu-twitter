package com.cmux.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.cmux.postservice.model.CommunityPost;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    CommunityPost findByCommunityPostid(long communityPostid);
    List<CommunityPost> findByAuthoridIn(List<Long> authorIds);
    
}
