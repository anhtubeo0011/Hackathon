package com.example.cms.repo;

import com.example.cms.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByProjectId(Long projectId);

  List<Task> findByEmployeeId(Long employeeId);

  @Query(
      value =
          "SELECT\n"
              + "        count(*)\n"
              + "    FROM\n"
              + "        EMPLOYEE e\n"
              + "    JOIN\n"
              + "        TASK t\n"
              + "            ON e.ID = t.EMPLOYEE_ID\n"
              + "    WHERE\n"
              + "        t.START_DATE >= TO_DATE(?, 'YYYY-MM-DD')\n"
              + "        AND t.START_DATE < TO_DATE(?, 'YYYY-MM-DD')\n"
              + "    HAVING\n"
              + "        SUM(t.ESTIMATE_HOURS) > 40",
      nativeQuery = true)
  Integer countOverloadEmployeeTasks(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  @Query(
      nativeQuery = true,
      value =
          "SELECT COUNT(CASE WHEN t.STATUS != 'COMPLETED' AND t.DUE_DATE < SYSDATE THEN 1 END) AS OVERDUE_TASKS\n"
              + "FROM TASK t")
  int countOverdueTasks();
}
