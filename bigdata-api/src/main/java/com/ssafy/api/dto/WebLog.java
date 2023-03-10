package com.ssafy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.events.EventTarget;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebLog {

    private String serviceToken;
    private String sessionId;
    private String event;
    private String targetId;
    private MousePosition position;
    private String location;
    private long timestamp;

}
