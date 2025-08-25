package com.example.cms.repository;

import com.example.cms.entity.Incident;
import com.example.cms.entity.IncidentStatus;
import com.example.cms.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    List<Incident> findByStatus(IncidentStatus status);
    
    List<Incident> findByPriority(Priority priority);
    
    List<Incident> findByErrorType(String errorType);
    
    List<Incident> findByAssignedDevId(Long developerId);
    
    @Query("SELECT i FROM Incident i WHERE i.title LIKE %:keyword% OR i.description LIKE %:keyword%")
    List<Incident> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT i FROM Incident i WHERE i.dueDate < :currentDate AND i.status NOT IN ('RESOLVED', 'CLOSED')")
    List<Incident> findOverdueIncidents(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT COUNT(i) FROM Incident i WHERE i.status = :status")
    Long countByStatus(@Param("status") IncidentStatus status);
    
    @Query("SELECT COUNT(i) FROM Incident i WHERE i.priority = :priority")
    Long countByPriority(@Param("priority") Priority priority);
    
    Incident findByIncidentId(String incidentId);
}