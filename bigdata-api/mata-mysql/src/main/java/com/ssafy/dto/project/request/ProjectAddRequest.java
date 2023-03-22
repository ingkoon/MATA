package com.ssafy.dto.project.request;

import com.ssafy.entity.Member;
import com.ssafy.entity.Project;
import com.ssafy.entity.enums.ProjectCategory;
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

