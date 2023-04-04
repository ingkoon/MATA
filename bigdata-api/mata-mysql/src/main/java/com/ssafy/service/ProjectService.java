package com.ssafy.service;

import com.ssafy.dto.member.exception.NoSuchMemberException;
import com.ssafy.dto.project.exception.NoSuchProjectException;
import com.ssafy.dto.project.request.ProjectAddRequest;
import com.ssafy.dto.project.request.ProjectRequest;
import com.ssafy.dto.project.response.ProjectResponse;
import com.ssafy.dto.project.response.TokenResponse;
import com.ssafy.entity.Member;
import com.ssafy.entity.Project;
import com.ssafy.repository.member.MemberRepository;
import com.ssafy.repository.project.ProjectRepository;
import com.ssafy.common.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final StringRedisTemplate stringRedisTemplate;
    private final Validation validation;

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

    public ProjectResponse getProjectDetail(Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(NoSuchProjectException::new);
        return ProjectResponse.fromEntity(project);
    }

    @Transactional
    public void delete(ProjectRequest request){
        Project project = getProject(request);
        projectRepository.delete(project);
    }


    @Transactional
    public TokenResponse updateToken(ProjectRequest request){
        Project project = getProject(request);

        if(project.getToken() != null) {
            stringRedisTemplate.delete(project.getToken());
        }
        project.updateToken();
        validation.setTokenToRedis(project.getToken(), project);
        log.info(project.getToken());
        return new TokenResponse().fromEntity(project);
    }
    @Transactional
    public void deleteToken(ProjectRequest request){
        Project project = getProject(request);
        project.deleteToken();
    }


    private Project getProject(ProjectRequest request){
        Long projectId = request.getProjectId();
        return projectRepository.findById(projectId).orElseThrow(NoSuchProjectException::new);
    }
}
