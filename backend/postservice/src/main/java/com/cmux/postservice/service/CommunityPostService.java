package com.cmux.postservice.service;

import java.util.NoSuchElementException;
import com.cmux.postservice.dto.CommunityPostDTO;
import com.cmux.postservice.handleException.IndexingException;
import com.cmux.postservice.model.CommunityPost;
import com.cmux.postservice.model.PostEvents;
import com.cmux.postservice.repository.CommunityPostRepository;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.cmux.postservice.converter.CommunityPostConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
public class CommunityPostService extends AbstractESService<CommunityPost> {

    private CommunityPostRepository communityPostRepository;
    private CommunityPostConverter communityPostConverter;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public CommunityPostService(CommunityPostRepository communityPostRepository,
            CommunityPostConverter communityPostConverter,
            ApplicationEventPublisher publisher, ElasticsearchClient elasticsearchClient) {
        super(elasticsearchClient);
        this.communityPostRepository = communityPostRepository;
        this.communityPostConverter = communityPostConverter;
        this.publisher = publisher;
    }

    public CommunityPostDTO savePost(CommunityPostDTO communityPostDTO) {

        CommunityPost communityPost = communityPostConverter.convertToEntity(communityPostDTO);
        communityPost = communityPostRepository.save(communityPost);

        publisher.publishEvent(new PostEvents.Created(communityPost));

        return communityPostConverter.convertToDTO(communityPost);
    }

    @Transactional
    public synchronized CommunityPost addLikeToPost(long communityPostId) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + communityPostId));
        communityPost.setLikes(communityPost.getLikes() + 1);
        communityPostRepository.save(communityPost);

        publisher.publishEvent(new PostEvents.Updated(communityPost));
        return communityPost;
    }

    
    @Transactional
    public Optional<CommunityPostDTO> getPostById(long id) {

        Optional<CommunityPost> post = communityPostRepository.findById(id);

        if (post.isPresent()) {

            CommunityPostDTO communityPostDTO = communityPostConverter.convertToDTO(post.get());

            return Optional.of(communityPostDTO);

        } else {

            throw new NoSuchElementException("Post not found for id: " + id);

        }
    }

    @Transactional
    public List<CommunityPostDTO> getPostsByAuthorId(List<Long> authorId_list) {
        List<CommunityPost> posts = communityPostRepository.findByAuthoridIn(authorId_list);

        List<CommunityPostDTO> communityPostDTOs = new ArrayList<CommunityPostDTO>();

        for (CommunityPost post : posts) {
            communityPostDTOs.add(communityPostConverter.convertToDTO(post));
        }

        return communityPostDTOs;
    }

    @Transactional
    public List<CommunityPostDTO> searchPosts(String query) {
        try {
            System.out.println("CommunityPostService: searchPosts: query: " + query);
            var searchResponse = elasticsearchClient.search(s -> s
                    .index("communitypost")
                    .query(q -> q
                        .queryString(d -> d
                            .query(query)
                        )
                    ),
                CommunityPost.class
            );

            List<CommunityPostDTO> result = new ArrayList<>();
            searchResponse.hits().hits().forEach(hit -> result.add(communityPostConverter.convertToDTO(hit.source())));
            return result;
        } catch (Exception e) {
            throw new IndexingException("Error during search: " + e.getMessage(), e);
        }
    }

    public void deletePostById(long communityPostId) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + communityPostId));
        communityPostRepository.delete(communityPost);

        // after delete in mysql, publish event for elastic search if needed
        publisher.publishEvent(new PostEvents.Deleted(communityPostId));
    }

    @Transactional
    public CommunityPostDTO updatePost(long communityPostId, CommunityPostDTO communityPostDTO) {
        CommunityPost existingPost = communityPostRepository.findById(communityPostId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + communityPostId));

        // Update entity with DTO details
        // Assuming you have method in your converter or service to update entity fields
        // from DTO
        existingPost = communityPostConverter.updateEntityWithDTO(existingPost, communityPostDTO);

        CommunityPost updatedPost = communityPostRepository.save(existingPost);

        // after update to mysql, publish event for elastic search
        publisher.publishEvent(new PostEvents.Updated(updatedPost));

        return communityPostConverter.convertToDTO(updatedPost);
    }

    // @Transactional
    // public CommunityPostDTO markAsFindTeammatePost(long communityPostId, CommunityPostDTO communityPostDTO) {
    //     CommunityPost communityPost = communityPostRepository.findById(communityPostId)
    //             .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + communityPostId));
    //     communityPost.setFindTeammatePost(true);

    //     communityPost.setCourseNumber(communityPostDTO.getCourseNumber());
    //     communityPost.setInstructorName(communityPostDTO.getInstructorName());
    //     communityPost.setSemester(communityPostDTO.getSemester());
    //     List<String> newTeamMembers = communityPost.getTeamMembers();
    //     if (newTeamMembers == null) {
    //         // instantiate new list
    //         newTeamMembers = new ArrayList<String>();
    //     }
    //     newTeamMembers.add(communityPostDTO.getTeamMembers());
    //     communityPost.setTeamMembers(newTeamMembers);

    //     communityPostRepository.save(communityPost);

    //     return communityPostConverter.convertToDTO(communityPost);
    // }

    @Transactional
    public CommunityPostDTO addTeamMembers(long communityPostId, String username) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + communityPostId));
        List<String> newTeamMembers = communityPost.getTeamMembers();
        if (newTeamMembers == null) {
            // instantiate new list
            newTeamMembers = new ArrayList<String>();
        }
        newTeamMembers.add(username);
        communityPost.setTeamMembers(newTeamMembers);

        communityPostRepository.save(communityPost);

        return communityPostConverter.convertToDTO(communityPost);

    }

    @Override
    public void index(String index, String id, CommunityPost communityPost) {
        try {
            System.out.println("Elasticsearch host: " + elasticsearchClient._transportOptions().toString());
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index("communitypost")
                    .id(String.valueOf(communityPost.getCommunityPostid()))
                    .document(communityPost));
            System.out.println("Communitypost: indexPost: indexed post");
        } catch (Exception e) {
            // Handle the exception
            throw new IndexingException("Cannot index post: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteIndex(String index, String id) {
        super.deleteIndex(index, id);

        System.out.println("Communitypost: delete index: " + id);
    }

}
