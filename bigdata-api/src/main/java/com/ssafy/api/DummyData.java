package com.ssafy.api;

import com.ssafy.api.entity.Member;
import com.ssafy.api.entity.Project;
import com.ssafy.api.entity.enums.ProjectCategory;
import com.ssafy.api.repository.member.MemberRepository;
import com.ssafy.api.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void run(String... args) throws Exception {
        addMember();
        addProject();
    }

    private void addProject() {
        System.out.println("addProject");
        List<Member> memberList = memberRepository.findAll();
        for (int i = 0; i < memberList.size(); i++) {
            for (int j = 0; j < 5; j++) {
                projectRepository.save(Project.builder()
                        .category(ProjectCategory.BLOG)
                        .url("ssafy.com/" + memberList.get(i).getName())
                        .name(memberList.get(i).getName() + "의 "+ j +"번째 프로젝트")
                        .member(memberList.get(i))
                        .build());
            }
        }
    }

    private void addMember() {
        System.out.println("addMember");
        for (int i = 0; i < 5; i++) {
            memberRepository.save(Member.builder()
                    .name("싸피맨"+i)
                    .email("ssafy"+i+"@ssafy.com")
                    .password("1234")
                    .build());
        }
    }


}
