package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.Domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

    FriendRequest findById(Integer id);

    void deleteById(Integer id);

}
