package com.socalite.scoalite.post.service;

import com.socalite.scoalite.post.model.ReactionCounts;
import com.socalite.scoalite.post.repo.PostStatsRepo;
import com.socalite.scoalite.reaction.model.ReactionType;
import com.socalite.scoalite.utils.Multi;
import com.socalite.scoalite.utils.PageUtils;
import com.socalite.scoalite.post.AllPostEntry;
import com.socalite.scoalite.post.PostRequestDTO;
import com.socalite.scoalite.post.RegisterPostResponseDTO;
import com.socalite.scoalite.post.converter.PostToAllPostEntry;
import com.socalite.scoalite.post.model.Post;
import com.socalite.scoalite.post.model.PostStats;
import com.socalite.scoalite.post.repo.PostRepo;
import com.socalite.scoalite.reaction.PostOwnerId;
import com.socalite.scoalite.user.model.User;
import com.socalite.scoalite.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@Transactional
public class PostService {

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private PostStatsRepo postStatsRepo;

    @Autowired
    private ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    public Multi<List<AllPostEntry>> getPosts(Long userId, Pageable pageRequest) {
        var toAllPostEntryConverterBean = applicationContext.getBean(PostToAllPostEntry.class);
        pageRequest = PageUtils.rebuild(pageRequest, Sort.by(desc("postedAt")));

        var posts = postRepo.findAllByOwnerId(userId, pageRequest);
        var allPostsEntries = posts.stream().map(toAllPostEntryConverterBean::convert).toList();
        return PageUtils.toMulti(posts, allPostsEntries);
    }

    public Optional<PostOwnerId> getPostOwnerId(long postId) {
        return postRepo.findById(postId)
                .map(Post::getOwner)
                .map(User::getId)
                .map(ownerId -> new PostOwnerId(ownerId, postId));
    }

    public RegisterPostResponseDTO post(long userId, PostRequestDTO postRequestDTO) {

        var userRefOptional = userService.getUserReference(userId);

        if (userRefOptional.isEmpty()) {
            return RegisterPostResponseDTO.error();
        }

        var userRef = userRefOptional.get();

        var post = new Post();
        post.setPostedAt(LocalDateTime.now());
        post.setComments(new LinkedHashSet<>());
        post.setReactions(new LinkedHashSet<>());
        post.setContent(postRequestDTO.getContent());
        post.setOwner(userRef);

        var postStats = new PostStats();
        postStats.setCommentsCount(0);
//        postStats.setReactionCounts(new ReactionCounts());
        postStats.setPost(post);

        post.setStats(postStats);

        var postId = postRepo.save(post).getId();;
        return RegisterPostResponseDTO.success(postId);
    }

    public Optional<Post> getPostRef(long postId) {
        if(postRepo.existsById(postId)) {
            return Optional.of(postRepo.getReferenceById(postId));
        }
        return Optional.empty();
    }

    public long incrementCommentCount(Post postRef) {
        var postStats = postRef.getStats();
        var incrementCommentCount = postStats.getCommentsCount() + 1;
        postStats.setCommentsCount(incrementCommentCount);
        postStatsRepo.save(postStats);
        return incrementCommentCount;
    }

    public long decrementCommentCount(Post postRef) {
        var postStats = postRef.getStats();
        var decrementCommentCount = postStats.getCommentsCount() - 1;
        postStats.setCommentsCount(decrementCommentCount);
        postStatsRepo.save(postStats);
        return decrementCommentCount;
    }

    public void incrementReactionCount(Post postRef, ReactionType reactionType) {
        postRef.getStats().incrementReactionCount(reactionType);
        postStatsRepo.save(postRef.getStats());
    }

    public void decrementReactionCount(Post postRef, ReactionType type) {
        postRef.getStats().decrementReactionCount(type);
        postStatsRepo.save(postRef.getStats());
    }
}
