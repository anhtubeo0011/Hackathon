package com.example.cms.repository;

import com.example.cms.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    
    Optional<Developer> findByEmail(String email);
    
    @Query("SELECT d FROM Developer d ORDER BY d.totalResolved DESC")
    List<Developer> findTopDevelopersByResolved();
    
    @Query("SELECT d FROM Developer d LEFT JOIN d.assignedIncidents i " +
           "WHERE i.status NOT IN ('RESOLVED', 'CLOSED') OR i.status IS NULL " +
           "GROUP BY d ORDER BY COUNT(i) ASC")
    List<Developer> findDevelopersOrderByActiveIncidents();
}