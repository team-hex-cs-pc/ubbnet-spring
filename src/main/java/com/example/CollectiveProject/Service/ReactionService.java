package com.example.CollectiveProject.Service;

import com.example.CollectiveProject.DTO.ReactionDTO;
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

import java.util.ArrayList;
import java.util.LinkedList;
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
	public Reaction addReaction(String postReference, String userName, Reaction.ReactionType type)
			throws com.example.CollectiveProject.Exceptions.NotFoundException {
		Post post = postRepository.findPostByPostReference(postReference);
		User user = userRepository.findUserByUsername(userName);

		if (user == null) {
			throw new com.example.CollectiveProject.Exceptions.NotFoundException("No user found!");
		}

		if (post == null) {
			throw new com.example.CollectiveProject.Exceptions.NotFoundException("No post found!");
		}

		if (type == null) {
			throw new com.example.CollectiveProject.Exceptions.NotFoundException("Reaction type not allowed!");
		}

		Reaction existingReaction = reactionRepository.findByPostAndUser(post, user).orElse(null);
		if (existingReaction == null) {
			existingReaction = new Reaction();
			existingReaction.setPost(post);
			existingReaction.setUser(user);
			existingReaction.setType(Reaction.ReactionType.LIKE);
			this.postService.likePost(postReference);
		} else {
			if (existingReaction.getType().equals(Reaction.ReactionType.LIKE)) {
				this.postService.dislikePost(postReference);
				existingReaction.setType(Reaction.ReactionType.DISLIKE);
			} else {
				this.postService.likePost(postReference);
				existingReaction.setType(Reaction.ReactionType.LIKE);
			}
		}
		return reactionRepository.save(existingReaction);
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
			if (reaction.getType() == Reaction.ReactionType.LIKE)
				postService.dislikePost(postReference);
			else
				postService.likePost(postReference);
			reactionRepository.delete(reaction);
		}
	}

	public List<ReactionDTO> getReactionsByPostReference(String postReference) throws com.example.CollectiveProject.Exceptions.NotFoundException {
		Post post = postRepository.findPostByPostReference(postReference);

		if (post == null) {
			throw new com.example.CollectiveProject.Exceptions.NotFoundException("Post not found!");
		}

		List<Reaction> reactions = reactionRepository.findAllByPost(post);
		List<ReactionDTO> reactionDTOS = new ArrayList<>();

		for (Reaction r : reactions) {
			reactionDTOS.add(ReactionDTO.fromEntity(r));
		}

		return reactionDTOS;
	}

	public List<ReactionDTO> getReactionsByUserId(Integer userId) throws com.example.CollectiveProject.Exceptions.NotFoundException {
		User user = userRepository.getUserByUserId(userId);

		if (user == null) {
			throw new com.example.CollectiveProject.Exceptions.NotFoundException("User not found!");
		}

		List<Reaction> reactions = reactionRepository.findAllByUser(user);
		List<ReactionDTO> reactionDTOS = new ArrayList<>();

		for (Reaction r : reactions) {
			reactionDTOS.add(ReactionDTO.fromEntity(r));
		}

		return reactionDTOS;
	}

	public ReactionDTO getReactionByPostReferenceAndUserId(String postReference, Integer userId) throws
			com.example.CollectiveProject.Exceptions.NotFoundException {
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

		return ReactionDTO.fromEntity(existingReaction);
	}
}



