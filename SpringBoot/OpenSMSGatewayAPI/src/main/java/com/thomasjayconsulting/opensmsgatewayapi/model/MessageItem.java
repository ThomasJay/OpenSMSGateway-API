package com.thomasjayconsulting.opensmsgatewayapi.model;

import lombok.Data;

@Data
public class MessageItem {
    private String phoneNumber;
    private String sender;
    private String message;

}
