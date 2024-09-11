package com.socalite.scoalite.user.service;

import com.socalite.scoalite.comment.service.CommentService;
import com.socalite.scoalite.post.service.PostService;
import com.socalite.scoalite.reaction.service.ReactionService;
import com.socalite.scoalite.user.converter.RegisterUserDTOToUserConverter;
import com.socalite.scoalite.user.dto.RegisterUserRequestDTO;
import com.socalite.scoalite.user.dto.RegisterUserResponseDTO;
import com.socalite.scoalite.user.model.User;
import com.socalite.scoalite.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ApplicationContext applicationContext;

    public boolean isConnected(long userId, long connectedUserId) {
        return true;
    }

    public Optional<User> getUserReference(long userId) {
        if (userRepo.existsById(userId)) {
            return Optional.of(userRepo.getReferenceById(userId));
        } else {
            return Optional.empty();
        }
    }

    public RegisterUserResponseDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        Objects.requireNonNull(registerUserRequestDTO);

        var user = applicationContext.getBean(RegisterUserDTOToUserConverter.class).convert(registerUserRequestDTO);

        var isDuplicateUsername = userRepo.existsByUsername(user.getUsername());
        if (isDuplicateUsername) {
            return new RegisterUserResponseDTO().setUserId(-1).setMessage("Duplicate Username");
        }

        var isDuplicateEmail = userRepo.existsByContactEmailAddress(user.getContact().getEmailAddress());
        if (isDuplicateEmail) {
            return new RegisterUserResponseDTO().setUserId(-1).setMessage("Duplicate Email");
        }

        var isDuplicatePhoneNumber = userRepo.existsByContactPhoneNumber(user.getContact().getPhoneNumber());
        if (isDuplicatePhoneNumber) {
            return new RegisterUserResponseDTO().setUserId(-1).setMessage("Duplicate Phone Number");
        }

        var userId = userRepo.save(user).getId();
        return new RegisterUserResponseDTO().setUserId(userId).setMessage("Registration Done");
    }
}
