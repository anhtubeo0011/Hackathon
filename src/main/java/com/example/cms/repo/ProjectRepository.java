package com.example.cms.repo;

import com.example.cms.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
  @Query(
      nativeQuery = true,
      value =
          "SELECT p.* FROM PROJECT p\n"
              + "    WHERE (:name IS NULL OR p.NAME LIKE '%' || :name || '%')\n"
              + "    AND (:status IS NULL OR p.STATUS = :status)\n"
              + "    AND (:pm IS NULL OR p.PM LIKE '%' || :pm || '%')")
  List<Project> findAll(
      @Param(value = "name") String name,
      @Param(value = "status") String status,
      @Param(value = "pm") String pm);

  @Query(
      nativeQuery = true,
      value =
          "SELECT\n"
              + "    CASE\n"
              + "        WHEN TRUNC(p.END_DATE - SYSDATE) < 0 THEN 'HIGH'  -- Quá hạn\n"
              + "        ELSE\n"
              + "            CASE\n"
              + "                -- Tính tỷ lệ: giờ còn lại / (ngày còn lại * 8 giờ/ngày)\n"
              + "                WHEN SUM(CASE WHEN t.STATUS != 'COMPLETED' THEN t.ESTIMATE_HOURS ELSE 0 END) /\n"
              + "                     (GREATEST(TRUNC(p.END_DATE - SYSDATE), 1) * 8) >= 1.5\n"
              + "                     OR COUNT(CASE WHEN t.STATUS != 'COMPLETED' THEN 1 END) > 10\n"
              + "                     OR TRUNC(p.END_DATE - SYSDATE) < 3 THEN 'HIGH'\n"
              + "                WHEN SUM(CASE WHEN t.STATUS != 'COMPLETED' THEN t.ESTIMATE_HOURS ELSE 0 END) /\n"
              + "                     (GREATEST(TRUNC(p.END_DATE - SYSDATE), 1) * 8) >= 1\n"
              + "                     OR (COUNT(CASE WHEN t.STATUS != 'COMPLETED' THEN 1 END) BETWEEN 5 AND 10\n"
              + "                         AND TRUNC(p.END_DATE - SYSDATE) BETWEEN 3 AND 7) THEN 'MEDIUM'\n"
              + "                ELSE 'LOW'\n"
              + "            END\n"
              + "    END AS RISK_LEVEL\n"
              + "FROM\n"
              + "    PROJECT p\n"
              + "LEFT JOIN\n"
              + "    TASK t ON p.ID = t.PROJECT_ID\n"
              + "WHERE\n"
              + "    p.STATUS != 'COMPLETED'\n"
              + "AND p.ID = :id\n"
              + "GROUP BY\n"
              + "    p.ID, p.NAME, p.END_DATE")
  String riskLevel(@Param("id") Long projectId);

  @Query(
      nativeQuery = true,
      value =
          "SELECT count(*)\n"
              + "FROM (SELECT p.ID                                                                    AS PROJECT_ID,\n"
              + "             p.NAME                                                                  AS PROJECT_NAME,\n"
              + "             p.END_DATE,\n"
              + "             TRUNC(p.END_DATE - SYSDATE)                                             AS DAYS_REMAINING,        -- Ngày còn lại (âm nếu quá hạn)\n"
              + "             COUNT(CASE WHEN t.STATUS != 'COMPLETED' THEN 1 END)                     AS TASKS_REMAINING,       -- Số task còn lại\n"
              + "             SUM(CASE WHEN t.STATUS != 'COMPLETED' THEN t.ESTIMATE_HOURS ELSE 0 END) AS TOTAL_HOURS_REMAINING, -- Tổng giờ còn lại\n"
              + "             CASE\n"
              + "                 WHEN TRUNC(p.END_DATE - SYSDATE) < 0 THEN 'HIGH' -- Quá hạn\n"
              + "                 ELSE\n"
              + "                     CASE\n"
              + "                         -- Tính tỷ lệ: giờ còn lại / (ngày còn lại * 8 giờ/ngày)\n"
              + "                         WHEN SUM(CASE WHEN t.STATUS != 'COMPLETED' THEN t.ESTIMATE_HOURS ELSE 0 END) /\n"
              + "                              (GREATEST(TRUNC(p.END_DATE - SYSDATE), 1) * 8) >= 1.5\n"
              + "                             OR COUNT(CASE WHEN t.STATUS != 'COMPLETED' THEN 1 END) > 10\n"
              + "                             OR TRUNC(p.END_DATE - SYSDATE) < 3 THEN 'HIGH'\n"
              + "                         WHEN SUM(CASE WHEN t.STATUS != 'COMPLETED' THEN t.ESTIMATE_HOURS ELSE 0 END) /\n"
              + "                              (GREATEST(TRUNC(p.END_DATE - SYSDATE), 1) * 8) >= 1\n"
              + "                             OR (COUNT(CASE WHEN t.STATUS != 'COMPLETED' THEN 1 END) BETWEEN 5 AND 10\n"
              + "                                 AND TRUNC(p.END_DATE - SYSDATE) BETWEEN 3 AND 7) THEN 'MEDIUM'\n"
              + "                         ELSE 'LOW'\n"
              + "                         END\n"
              + "                 END                                                                 AS RISK_LEVEL\n"
              + "      FROM PROJECT p\n"
              + "               LEFT JOIN\n"
              + "           TASK t ON p.ID = t.PROJECT_ID\n"
              + "      WHERE p.STATUS != 'COMPLETED' -- Chỉ xem project đang hoạt động (giả sử STATUS project có 'COMPLETED')\n"
              + "   AND (:projectId IS NULL OR p.ID = :projectId)\n"
              + "  AND (:status IS NULL OR p.STATUS = :status)\n"
              + "      GROUP BY p.ID, p.NAME, p.END_DATE\n"
              + "      ORDER BY CASE RISK_LEVEL WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 END,\n"
              + "               DAYS_REMAINING ASC\n"
              + ") t\n"
              + "WHERE t.RISK_LEVEL = 'HIGH'")
  Integer numberOfHighRiskLevels(
      @Param("projectId") Long projectId, @Param("status") String status);

  @Query(
      nativeQuery = true,
      value =
          "SELECT ROUND(\n"
              + "               (COUNT(CASE WHEN t.STATUS = 'COMPLETED' AND t.START_DATE <= t.DUE_DATE THEN 1 END) * 100.0 /\n"
              + "                NULLIF(COUNT(t.ID), 0)),\n"
              + "               2\n"
              + "       ) AS COMPLETION_RATE_PERCENT\n"
              + "FROM PROJECT p\n"
              + "         LEFT JOIN\n"
              + "     TASK t ON p.ID = t.PROJECT_ID\n"
              + "WHERE (:projectId IS NULL OR p.ID = :projectId)\n"
              + "AND (:status IS NULL OR p.STATUS = :status)")
  Float completionRatePercent(@Param("projectId") Long projectId, @Param("status") String status);
}
