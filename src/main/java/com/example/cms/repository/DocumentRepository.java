package com.example.cms.repository;

import com.example.cms.entity.Document;
import com.example.cms.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByErrorType(String errorType);
    
    List<Document> findByPriority(Priority priority);
    
    @Query("SELECT d FROM Document d WHERE d.title LIKE %:keyword% OR d.content LIKE %:keyword%")
    List<Document> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT d FROM Document d WHERE d.errorType = :errorType ORDER BY d.priority DESC, d.updatedAt DESC")
    List<Document> findByErrorTypeOrderByPriorityAndDate(@Param("errorType") String errorType);
    
    Document findByDocumentId(String documentId);
}