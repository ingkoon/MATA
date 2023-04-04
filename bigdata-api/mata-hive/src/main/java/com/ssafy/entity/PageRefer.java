package com.ssafy.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PageRefer {

    private int totalSession;
    private long totalPageenter;
    private Timestamp updateTimestamp;
    private String referrer;
    private long serviceId;

}
