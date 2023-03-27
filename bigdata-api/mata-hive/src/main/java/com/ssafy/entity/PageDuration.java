package com.ssafy.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PageDuration {

    private long totalDuration;
    private int totalSession;
    private String location;
    private Timestamp updateTimestamp;
    private long serviceId;

}
