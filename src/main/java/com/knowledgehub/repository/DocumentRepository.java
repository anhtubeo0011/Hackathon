package com.knowledgehub.repository;

import com.knowledgehub.entity.Document;
import com.knowledgehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByAuthor(User author);
    
    @Query("SELECT d FROM Document d WHERE " +
           "(d.privacy = 'PUBLIC') OR " +
           "(d.privacy = 'GROUP' AND d.author.groupName = :groupName) OR " +
           "(d.privacy = 'PRIVATE' AND d.author.id = :userId)")
    Page<Document> findVisibleDocuments(@Param("userId") Long userId, 
                                       @Param("groupName") String groupName, 
                                       Pageable pageable);
    
    @Query("SELECT d FROM Document d WHERE " +
           "((d.privacy = 'PUBLIC') OR " +
           "(d.privacy = 'GROUP' AND d.author.groupName = :groupName) OR " +
           "(d.privacy = 'PRIVATE' AND d.author.id = :userId)) AND " +
           "(LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.summary) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Document> findVisibleDocumentsWithSearch(@Param("userId") Long userId,
                                                 @Param("groupName") String groupName,
                                                 @Param("search") String search,
                                                 Pageable pageable);
    
    @Query("SELECT d FROM Document d JOIN d.tags t WHERE " +
           "((d.privacy = 'PUBLIC') OR " +
           "(d.privacy = 'GROUP' AND d.author.groupName = :groupName) OR " +
           "(d.privacy = 'PRIVATE' AND d.author.id = :userId)) AND " +
           "t IN :tags")
    Page<Document> findVisibleDocumentsByTags(@Param("userId") Long userId,
                                            @Param("groupName") String groupName,
                                            @Param("tags") List<String> tags,
                                            Pageable pageable);
}