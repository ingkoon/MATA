package com.ssafy.service;

import com.ssafy.dto.member.exception.NoSuchMemberException;
import com.ssafy.dto.project.exception.NoSuchProjectException;
import com.ssafy.dto.project.request.ProjectAddRequest;
import com.ssafy.dto.project.request.ProjectDeleteRequest;
import com.ssafy.dto.project.response.ProjectResponse;
import com.ssafy.entity.Member;
import com.ssafy.entity.Project;
import com.ssafy.repository.member.MemberRepository;
import com.ssafy.repository.project.ProjectRepository;
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
