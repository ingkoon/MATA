package com.ssafy.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Component {

    private int totalClick;
    private String targetId;
    private String location;
    private Timestamp updateTimestamp;
    private long serviceId;

}
