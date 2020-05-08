package com.dhbwstudent.brainstormbe.wss.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketResponse {
    private String content;
    private String type;
}
