package com.ssafy.api.dto.project.request;

import com.ssafy.api.entity.mysql.Member;
import com.ssafy.api.entity.mysql.Project;
import com.ssafy.api.entity.mysql.enums.ProjectCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAddRequest {

    private String url;
    private String name;
    private ProjectCategory category;


    public Project toEntity(Member member) {
        Project project = Project.builder()
                .category(category)
                .url(url)
                .member(member)
                .name(name)
                .build();
        return project;
    }
}

