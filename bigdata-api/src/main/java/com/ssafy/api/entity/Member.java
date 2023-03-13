package com.ssafy.api.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

    @Id @Column(name = "memberId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    @NotNull
    private String password;

    @Size(max = 50)
    @NotNull
    private String email;

    @Size(max = 10)
    @NotNull
    private String name;

    @CreationTimestamp
    private LocalDateTime createAt;

    @ColumnDefault("false")
    private boolean isQuit;

    @OneToMany(mappedBy = "member")
    private List<Project> projectList =new ArrayList<>();

    @Builder
    public Member(String password, String email, String name) {
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
