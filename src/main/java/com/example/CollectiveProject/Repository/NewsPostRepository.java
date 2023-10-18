package com.example.CollectiveProject.Repository;

import com.example.CollectiveProject.Domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsPostRepository extends JpaRepository<News, Integer> {
}
