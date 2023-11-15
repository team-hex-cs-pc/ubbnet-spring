package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.Domain.FriendRelation;
import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendRelation, Long> {
}
