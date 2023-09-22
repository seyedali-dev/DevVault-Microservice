package com.dev.vault.CommentService.repository;

import com.dev.vault.CommentService.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}