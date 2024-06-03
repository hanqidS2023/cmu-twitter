package com.cmux.postservice.controller;

import com.cmux.postservice.dto.CommunityPostDTO;
import com.cmux.postservice.service.CommunityPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/community")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CommunityPostDTO postDTO) {
        String dateNow = java.time.LocalDate.now().toString();
        postDTO.setCreated_Date(dateNow);
        communityPostService.savePost(postDTO);

        return new ResponseEntity<>("Post created successfully", HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CommunityPostDTO>> searchPosts(@RequestParam String query) {
        try {
            List<CommunityPostDTO> searchResults = communityPostService.searchPosts(query);
            return new ResponseEntity<>(searchResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/likes/{communityPostId}")
    public ResponseEntity<?> addLike(@PathVariable long communityPostId) {
        try {
            communityPostService.addLikeToPost(communityPostId);
            return new ResponseEntity<>("Like added successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error adding like", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{communityPostid}")
    public CommunityPostDTO getPostById(@PathVariable long communityPostid) {
        return communityPostService.getPostById(communityPostid)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + communityPostid));
    }

    @GetMapping("/authors/{authorId_list}")
    public List<CommunityPostDTO> getPostsByAuthorId(@PathVariable List<Long> authorId_list) {
        return communityPostService.getPostsByAuthorId(authorId_list);
    }

    @DeleteMapping("/{communityPostId}")
    public ResponseEntity<?> deletePost(@PathVariable long communityPostId) {
        try {
            communityPostService.deletePostById(communityPostId);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{communityPostId}")
    public ResponseEntity<?> updatePost(@PathVariable long communityPostId, @RequestBody CommunityPostDTO postDTO) {
        try {
            CommunityPostDTO updatedPost = communityPostService.updatePost(communityPostId, postDTO);
            System.out.println("updatedPost controller");
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        // } catch (AccessDeniedException e) {
        // return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        // }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating post" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/find-teammate/{communityPostId}")
    // public ResponseEntity<?> markAsFindTeammatePost(@PathVariable Long communityPostId,
    //         @RequestBody CommunityPostDTO communityPostDTO) {

    //     communityPostService.markAsFindTeammatePost(communityPostId, communityPostDTO);

    //     return new ResponseEntity<>("Add find teammate post successfully", HttpStatus.OK);
    // }

    @PutMapping("/{postId}/team-members")
    public ResponseEntity<?> addTeamMembers(@PathVariable Long postId,
            @PathVariable String username) {

        communityPostService.addTeamMembers(postId, username);

        return new ResponseEntity<>("Add team members successfully", HttpStatus.OK);
    }
}