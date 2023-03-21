package com.ssafy.api.entity.hive;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class ProjectManagement {

    @Id
    private long id;

    private String url;

    @OneToMany
    private List<PageMovement> pageMovementList;
}
