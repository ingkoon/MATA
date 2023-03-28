package com.ssafy;

import com.ssafy.entity.Member;
import com.ssafy.entity.Project;
import com.ssafy.entity.enums.MemberPrivilege;
import com.ssafy.entity.enums.ProjectCategory;
import com.ssafy.repository.member.MemberRepository;
import com.ssafy.repository.project.ProjectRepository;
import com.ssafy.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final MemberRepository memberRepository;

    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

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
                        .name(memberList.get(i).getName() + "s "+ j +" project")
                        .member(memberList.get(i))
                        .build());
            }
        }
    }

    private void addMember() {
        System.out.println("addMember");

        for (int i = 0; i < 5; i++) {
            Member member = Member.builder().name("ssafyman"+i)
                    .email("ssafy"+i+"@ssafy.com")
                    .password(passwordEncoder.encode("1234"))
                    .privilege(Collections.singleton(MemberPrivilege.GENERAL.name()))
                    .build();
            memberRepository.save(member);
        }
    }
}
