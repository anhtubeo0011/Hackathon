package com.example.cms.repo;

import com.example.cms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT e.* FROM EMPLOYEE e\n"
              + "    LEFT JOIN TASK t ON t.EMPLOYEE_ID = e.ID\n"
              + "    WHERE (:name IS NULL OR e.NAME LIKE '%' || :name || '%')\n"
              + "    AND (:mail IS NULL OR e.MAIL LIKE '%' || :mail || '%')\n"
              + "    AND (:skill IS NULL OR e.STRENGTH LIKE '%' || :skill || '%')\n"
              + "    AND (:projectId IS NULL OR t.ID = :projectId)\n"
              + "    GROUP BY e.ID, e.NAME, MAIL, STRENGTH, AVATAR")
  List<Employee> findAll(
      @Param("name") String name,
      @Param("mail") String email,
      @Param("skill") String skill,
      @Param("projectId") Long projectId);

  @Query(
      value =
          "SELECT e.*\n"
              + "FROM EMPLOYEE e\n"
              + "         INNER JOIN TASK t\n"
              + "                    ON e.ID = t.EMPLOYEE_ID\n"
              + "         INNER JOIN PROJECT p\n"
              + "                    ON t.PROJECT_ID = p.ID\n"
              + "WHERE t.START_DATE >= TO_DATE(:startDate, 'YYYY-MM-DD')\n"
              + "  AND t.START_DATE < TO_DATE(:endDate, 'YYYY-MM-DD')\n"
              + "  AND (:projectId IS NULL OR p.ID = :projectId)\n"
              + "  AND (:status IS NULL OR p.STATUS = :status)\n"
              + "GROUP BY e.ID, e.NAME, MAIL, STRENGTH, AVATAR\n"
              + "HAVING SUM(t.ESTIMATE_HOURS) > 40",
      nativeQuery = true)
  List<Employee> countOverloadEmployeeTasks(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("projectId") Long projectId,
      @Param("status") String status);
}
