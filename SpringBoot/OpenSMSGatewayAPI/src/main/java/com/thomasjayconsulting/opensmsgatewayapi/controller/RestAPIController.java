package com.thomasjayconsulting.opensmsgatewayapi.controller;

import com.thomasjayconsulting.opensmsgatewayapi.model.MessageDTO;
import com.thomasjayconsulting.opensmsgatewayapi.model.MessageItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thomasjayconsulting.opensmsgatewayapi.service.SMSMessageService;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class RestAPIController {

    private static final int MAX_MESSAGE_ITEMS = 5;

    private SMSMessageService smsMessageService;

    public RestAPIController(SMSMessageService smsMessageService) {
        this.smsMessageService = smsMessageService;
    }


    @PostMapping("/sendSMS")
    public ResponseEntity<?> sendSMS(@RequestBody MessageItem body) {
        log.info("Received sendSMS request body: {}", body);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender(body.getSender());
        messageDTO.setMessage(body.getMessage());
        messageDTO.setPhoneNumber(body.getPhoneNumber());

        messageDTO.setStatus("Queued");

        smsMessageService.queueSendMessage(messageDTO);

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", "SMS Queued successfully");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseMap);
    }


    @PostMapping("/checkSMSSendQueue/{phoneNumber}")
    public ResponseEntity<?> checkSMSSendQueue(@RequestBody String body, @PathVariable String phoneNumber){

        log.info("checkSMSSendQueue request phoneNumber: {}", phoneNumber);


        List<MessageItem> messageQueue = new ArrayList<>();

        // Get up to MAX_MESSAGE_ITEMS items from the queue to be sent
        for (int i=0;i<MAX_MESSAGE_ITEMS;i++) {
            MessageDTO messageQueueItem = smsMessageService.checkSMSSendQueueByPhoneNumber(phoneNumber);

            if (messageQueueItem == null) {
                break;
            }

            MessageItem messageItem = new MessageItem();
            messageItem.setPhoneNumber(messageQueueItem.getPhoneNumber());
            messageItem.setSender(messageQueueItem.getSender());
            messageItem.setMessage(messageQueueItem.getMessage());

            messageQueue.add(messageItem);
        }


        if (messageQueue.isEmpty()) {
            log.info("checkSMSSendQueue request phoneNumber: {} No Data Found", phoneNumber);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("");
        }

        else {
            Map<String, Object> responseMap = new HashMap<>();

            responseMap.put("items", messageQueue);

            log.info("checkSMSSendQueue request phoneNumber: {} {} items found}", phoneNumber, messageQueue.size());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);

        }

    }

    @PostMapping("/receivedSMS")
    public ResponseEntity<?> receivedSMS(@RequestBody MessageItem receviedSMS) {

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender(receviedSMS.getSender());
        messageDTO.setMessage(receviedSMS.getMessage());
        messageDTO.setPhoneNumber(receviedSMS.getPhoneNumber());

        messageDTO.setStatus("Queued");

        smsMessageService.queueReceivedMessage(messageDTO);

        return ResponseEntity.ok().body("");
    }

}
