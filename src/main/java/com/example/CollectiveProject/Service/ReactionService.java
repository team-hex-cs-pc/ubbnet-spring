package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.Domain.Post;
import com.example.CollectiveProject.Domain.Reaction;
import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Repository.PostRepository;
import com.example.CollectiveProject.Repository.ReactionRepository;
import com.example.CollectiveProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class ReactionService {

    @Autowired
    private final ReactionRepository reactionRepository;

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PostService postService;


    //TODO ADD REACTION DTOS + MAKE FUNCTIONS RETURN BOOLEAN
    public Reaction addReaction(String postReference, Integer userId, Reaction.ReactionType type) throws com.example.CollectiveProject.Exceptions.NotFoundException {
        Post post = postRepository.findPostByPostReference(postReference);
        User user = userRepository.getUserByUserId(userId);

        if (user == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("No user found!");
        }

        if (post == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("No post found!");
        }

        Reaction existingReaction = reactionRepository.findByPostAndUser(post, user).orElse(null);

        if (existingReaction != null) {
            //Moving from a like to a dislike counts as giving an unlike (-1 like) + dislike (-1 like)
            if(existingReaction.getType() == Reaction.ReactionType.LIKE && type == Reaction.ReactionType.DISLIKE) {
                postService.dislikePost(postReference);
                postService.dislikePost(postReference);
            }
            //Moving from a dislike to a like counts as giving an like (+1 like) + like (+1 like)
            else if(existingReaction.getType() == Reaction.ReactionType.DISLIKE && type == Reaction.ReactionType.LIKE) {
                postService.likePost(postReference);
                postService.likePost(postReference);
            }
            existingReaction.setType(type);
            return reactionRepository.save(existingReaction);
        }

        //if user didn't react to a post already with either like or dislike
        if(type == Reaction.ReactionType.LIKE) {
            postService.likePost(postReference);
        } else {
            postService.dislikePost(postReference);
        }
        Reaction newReaction = new Reaction();
        newReaction.setPost(post);
        newReaction.setUser(user);
        newReaction.setType(type);

        return reactionRepository.save(newReaction);
    }

    public void removeReaction(String postReference, Integer userId) throws com.example.CollectiveProject.Exceptions.NotFoundException {
        Post post = postRepository.findPostByPostReference(postReference);
        User user = userRepository.getUserByUserId(userId);

        if (user == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("No user found!");
        }

        if (post == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("No post found!");
        }


        Reaction existingReaction = reactionRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new NotFoundException("Reaction not found"));

        if (existingReaction.getType() == Reaction.ReactionType.LIKE)
            postService.dislikePost(postReference);
        else
            postService.likePost(postReference);

        reactionRepository.delete(existingReaction);
    }

    public void deleteReactionsByPostReference(String postReference) throws com.example.CollectiveProject.Exceptions.NotFoundException {
        Post post = postRepository.findPostByPostReference(postReference);

        if (post == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Post not found!");
        }

        List<Reaction> reactions = reactionRepository.findAllByPost(post);

        if (reactions == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Reactions not found!");
        }

        for (Reaction reaction : reactions) {
            if(reaction.getType() == Reaction.ReactionType.LIKE)
                postService.dislikePost(postReference);
            else
                postService.likePost(postReference);
            reactionRepository.delete(reaction);
        }
    }

    public List<Reaction> getReactionsByPostReference(String postReference) throws com.example.CollectiveProject.Exceptions.NotFoundException {
        Post post = postRepository.findPostByPostReference(postReference);

        if (post == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Post not found!");
        }

        List<Reaction> reactions = reactionRepository.findAllByPost(post);

        if (reactions == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Reactions not found!");
        }

        return reactions;
    }

    public List<Reaction> getReactionsByUserId(Integer userId) throws com.example.CollectiveProject.Exceptions.NotFoundException {
        User user = userRepository.getUserByUserId(userId);

        if (user == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("User not found!");
        }

        List<Reaction> reactions = reactionRepository.findAllByUser(user);

        if (reactions == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Reactions not found!");
        }

        return reactions;
    }

    public Reaction getReactionByPostReferenceAndUserId(String postReference, Integer userId) throws com.example.CollectiveProject.Exceptions.NotFoundException {
        Post post = postRepository.findPostByPostReference(postReference);
        User user = userRepository.getUserByUserId(userId);

        if (user == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("User not found!");
        }

        if (post == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Post not found!");
        }

        Reaction existingReaction = reactionRepository.findByPostAndUser(post, user).orElse(null);

        if (existingReaction == null) {
            throw new com.example.CollectiveProject.Exceptions.NotFoundException("Reaction not found!");
        }

        return existingReaction;
    }
}



