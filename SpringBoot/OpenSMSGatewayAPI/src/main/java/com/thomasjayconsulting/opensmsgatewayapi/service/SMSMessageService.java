package com.thomasjayconsulting.opensmsgatewayapi.service;

import com.thomasjayconsulting.opensmsgatewayapi.model.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SMSMessageService {

    private static final String SEND_MESSAGE_QUEUE = "send_message_queue";
    private static final String RECEIVED_MESSAGE_QUEUE = "received_message_queue";

    @Autowired
    MongoTemplate mongoTemplate;

    public void queueSendMessage(MessageDTO messageDTO) {
        log.info("queueSendMessage for phone number: {}", messageDTO.getPhoneNumber());

        mongoTemplate.save(messageDTO, SEND_MESSAGE_QUEUE);
    }

    public void queueReceivedMessage(MessageDTO messageDTO) {
        mongoTemplate.save(messageDTO, RECEIVED_MESSAGE_QUEUE);
    }

    public MessageDTO checkSMSSendQueueByPhoneNumber(String phoneNumber) {

        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("Queued"));
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));

        Update update = new Update().set("status", "Processed");

        return mongoTemplate.findAndModify(query, update, MessageDTO.class, SEND_MESSAGE_QUEUE);

    }






}
