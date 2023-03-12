package com.ssafy.api.service;

import com.ssafy.api.dto.member.exception.NoSuchMemberException;
import com.ssafy.api.dto.project.request.ProjectAddRequest;
import com.ssafy.api.dto.project.response.ProjectResponse;
import com.ssafy.api.entity.Member;
import com.ssafy.api.entity.Project;
import com.ssafy.api.repository.member.MemberRepository;
import com.ssafy.api.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    public void addProject(String email, ProjectAddRequest request) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NoSuchMemberException::new);
        Project project = request.toEntity(member);
        projectRepository.save(project);
    }

    public List<ProjectResponse> getList(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NoSuchMemberException::new);
        List<ProjectResponse> response = new ArrayList<>();
        return member.getProjectList().stream()

                .map(project -> new ProjectResponse().fromEntity(project))
                .collect(Collectors.toList());
    }
}
