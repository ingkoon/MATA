package com.ssafy.dto.project.response;

import com.ssafy.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    String token;

    public TokenResponse fromEntity(Project project){
        TokenResponse response = TokenResponse.builder()
                .token(project.getToken())
                .build();
        return response;
    }
}
