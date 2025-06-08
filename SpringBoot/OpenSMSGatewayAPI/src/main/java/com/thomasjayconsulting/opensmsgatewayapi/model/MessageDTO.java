package com.thomasjayconsulting.opensmsgatewayapi.model;

import lombok.Data;

@Data
public class MessageDTO {
    private String id;
    private String phoneNumber;
    private String sender;
    private String message;
    private String status;
    private String timestamp;

}
