package com.ssafy.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PageJournal {

    private int totalJournal;
    private String locationFrom;
    private String locationTo;
    private Timestamp updateTimestamp;
    private long serviceId;

}
