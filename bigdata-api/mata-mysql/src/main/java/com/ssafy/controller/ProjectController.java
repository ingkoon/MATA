package com.ssafy.controller;


import com.ssafy.config.sercurity.SecurityUtils;
import com.ssafy.dto.project.request.ProjectAddRequest;
import com.ssafy.dto.project.request.ProjectDeleteRequest;
import com.ssafy.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/")
    public ResponseEntity<?> projectList(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            return new ResponseEntity<>(projectService.getList(email), HttpStatus.OK);
        }  catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 프로젝트 추가
    @PostMapping("/add")
    public ResponseEntity<Void> addProject(@RequestBody ProjectAddRequest request){
        String email = SecurityUtils.getCurrentMemberEmail();
        log.info("email is : "+ email);
        projectService.addProject(email, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProject(@RequestBody ProjectDeleteRequest request){
        projectService.delete(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Delete Success");
    }

    // 프로젝트 삭제

}
