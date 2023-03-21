package com.ssafy.api.repository.hive;

import com.ssafy.api.entity.hive.ProjectManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectManagementRepository extends JpaRepository<ProjectManagement, Long> {
}
