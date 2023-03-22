package com.ssafy.api.dto.project.response;

import com.ssafy.api.entity.mysql.Project;
import com.ssafy.api.entity.mysql.enums.ProjectCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private String url;
    private String name;
    private LocalDateTime createAt;
    private ProjectCategory category;
    private String token;

    public static ProjectResponse fromEntity(Project project) {
        return ProjectResponse.builder()
                .url(project.getUrl())
                .name(project.getName())
                .createAt(project.getCreateAt())
                .category(project.getCategory())
                .token(project.getToken())
                .build();
    }
}
