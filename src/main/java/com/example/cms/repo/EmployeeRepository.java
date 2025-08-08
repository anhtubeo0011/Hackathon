package com.example.cms.repo;

import com.example.cms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
  @Query("SELECT id FROM Employee WHERE name = :name")
  Long findByName(@Param(value = "name") String name);
}
