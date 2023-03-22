package com.ssafy.api.repository.mysql;

import com.ssafy.api.entity.mysql.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
