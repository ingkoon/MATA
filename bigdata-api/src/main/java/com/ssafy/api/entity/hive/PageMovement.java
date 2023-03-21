package com.ssafy.api.entity.hive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PageMovement {
    @Id
    private long id;
    private long moveCount;
    private String fromUrl;
    private String toUrl;
    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectManagement projectManagement;
}
