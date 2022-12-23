package com.younglog.repository;

import com.younglog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom{

}
