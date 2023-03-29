package com.ssafy.dto.project.response;

import com.ssafy.entity.Project;
import com.ssafy.entity.enums.ProjectCategory;
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
    private Long id;
    private String url;
    private String name;
    private LocalDateTime createAt;
    private ProjectCategory category;
    private String token;

    public static ProjectResponse fromEntity(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .url(project.getUrl())
                .name(project.getName())
                .createAt(project.getCreateAt())
                .category(project.getCategory())
                .token(project.getToken())
                .build();
    }
}
