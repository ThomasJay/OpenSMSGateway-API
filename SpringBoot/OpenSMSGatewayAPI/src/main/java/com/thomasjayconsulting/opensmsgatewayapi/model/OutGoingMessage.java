package com.thomasjayconsulting.opensmsgatewayapi.model;

import lombok.Data;

@Data
public class OutGoingMessage {
    private String from;
    private String to;
    private String message;
}
