package com.ssafy.api.controller;

import com.ssafy.api.config.sercurity.SecurityUtils;
import com.ssafy.api.dto.project.request.ProjectAddRequest;
import com.ssafy.api.dto.project.response.ProjectResponse;
import com.ssafy.api.service.CustomUserDetailsService;
import com.ssafy.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/")
    public ResponseEntity<?> projectList(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            System.out.println(userDetails.getUsername());
//            return new ResponseEntity<>(projectService.getList(), HttpStatus.OK);
            return new ResponseEntity<>(userDetails.getUsername(), HttpStatus.OK);
        }  catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 프로젝트 추가
    @PostMapping("/add")
    public ResponseEntity<Void> addProject(@RequestBody ProjectAddRequest request){

        String email = SecurityUtils.getCurrentMemberEmail();
        projectService.addProject(email, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    // 프로젝트 삭제


}
