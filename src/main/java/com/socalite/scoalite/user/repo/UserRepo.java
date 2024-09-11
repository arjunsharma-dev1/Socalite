package com.socalite.scoalite.user.repo;

import com.socalite.scoalite.user.model.EmailAddress;
import com.socalite.scoalite.user.model.PhoneNumber;
import com.socalite.scoalite.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("from User u JOIN FETCH u.contact")
    Optional<User> findByIdAndContact();

    @EntityGraph(value = "User.Reactions.Comments")
    @Query("FROM User u")
    Optional<User> findAllWithReactionsAndComments();

    boolean existsByUsername(String username);

    boolean existsByContactEmailAddress(EmailAddress emailAddress);

    boolean existsByContactPhoneNumber(PhoneNumber phoneNumber);
}
