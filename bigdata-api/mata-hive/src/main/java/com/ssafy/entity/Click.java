package com.ssafy.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Click {

    private int totalClick;
    private int positionX;
    private int positionY;
    private String location;
    private Timestamp updateTimestamp;
    private long serviceId;

}
