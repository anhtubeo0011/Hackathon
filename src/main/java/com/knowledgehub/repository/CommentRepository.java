package com.knowledgehub.repository;

import com.knowledgehub.entity.Comment;
import com.knowledgehub.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDocumentOrderByCreatedAtDesc(Document document);
}