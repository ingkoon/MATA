package com.ssafy.api.service;

import com.ssafy.api.dto.member.exception.NoSuchMemberException;
import com.ssafy.api.dto.project.exception.NoSuchProjectException;
import com.ssafy.api.dto.project.request.ProjectAddRequest;
import com.ssafy.api.dto.project.request.ProjectDeleteRequest;
import com.ssafy.api.dto.project.response.ProjectResponse;
import com.ssafy.api.entity.mysql.Member;
import com.ssafy.api.entity.mysql.Project;
import com.ssafy.api.repository.mysql.MemberRepository;
import com.ssafy.api.repository.mysql.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addProject(String email, ProjectAddRequest request) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NoSuchMemberException::new);
        log.info(member.toString());
        Project project = request.toEntity(member);
        projectRepository.save(project);
    }

    public List<ProjectResponse> getList(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NoSuchMemberException::new);
        return member.getProjectList().stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(ProjectDeleteRequest request){
        Long projectId = request.getProjectId();
        Project project = projectRepository.findById(projectId).orElseThrow(NoSuchProjectException::new);
        projectRepository.delete(project);
    }
}
