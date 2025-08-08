package com.example.cms.repo;

import com.example.cms.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  @Query("SELECT id FROM Project WHERE name = :name")
  Long findByName(@Param(value = "name") String name);
}
