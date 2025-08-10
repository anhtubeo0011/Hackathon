package com.example.cms.repo;

import com.example.cms.entity.Employee;
import com.example.cms.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  @Query(
      nativeQuery = true,
      value =
          "SELECT t.* FROM TASK t\n"
              + "    WHERE (:name IS NULL OR t.NAME LIKE '%' || :name || '%')\n"
              + "    AND (:requiredSkills IS NULL OR t.REQUIRED_SKILLS LIKE '%' || :requiredSkills || '%')\n"
              + "    AND (:projectId IS NULL OR t.PROJECT_ID = :projectId)\n"
              + "    AND (:employeeId IS NULL OR t.EMPLOYEE_ID = :employeeId)\n"
              + "    AND (:status IS NULL OR t.STATUS = :status)")
  List<Task> findAll(
      @Param("name") String name,
      @Param("requiredSkills") String requiredSkills,
      @Param("projectId") Long projectId,
      @Param("employeeId") Long employeeId,
      @Param("status") String status);

  @Query(
      nativeQuery = true,
      value =
          "SELECT t.* FROM TASK t\n" + "WHERE (:projectId IS NULL OR t.PROJECT_ID = :projectId)")
  List<Task> findByProjectId(@Param("projectId") Long projectId);

  List<Task> findByEmployeeId(Long employeeId);

  @Query(
      nativeQuery = true,
      value =
          "SELECT COUNT(CASE WHEN t.STATUS != 'COMPLETED' AND t.DUE_DATE < SYSDATE THEN 1 END) AS OVERDUE_TASKS\n"
              + "FROM TASK t\n"
              + "         INNER JOIN PROJECT p ON t.PROJECT_ID = p.ID\n"
              + "WHERE (:projectId IS NULL OR p.ID = :projectId)\n"
              + "  AND (:status IS NULL OR p.STATUS = :status)")
  int countOverdueTasks(@Param("projectId") Long projectId, @Param("status") String status);
}
