package com.dev.vault.CommentService.repository;

import com.dev.vault.CommentService.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCommentedOnProjectId(long commentedOnProjectId);

}