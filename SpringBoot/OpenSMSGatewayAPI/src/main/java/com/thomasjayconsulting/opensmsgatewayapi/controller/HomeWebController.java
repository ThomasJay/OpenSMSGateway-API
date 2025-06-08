package com.thomasjayconsulting.opensmsgatewayapi.controller;

import com.thomasjayconsulting.opensmsgatewayapi.model.MessageDTO;
import com.thomasjayconsulting.opensmsgatewayapi.model.OutGoingMessage;
import com.thomasjayconsulting.opensmsgatewayapi.service.SMSMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class HomeWebController {

    private SMSMessageService smsMessageService;

    public HomeWebController(SMSMessageService smsMessageService) {
        this.smsMessageService = smsMessageService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("outGoingMessage", new OutGoingMessage());
        return "index";
    }

    @PostMapping("/sendSMS")
    public String sendSMS(@ModelAttribute("outGoingMessage") OutGoingMessage outGoingMessage, Model model) {

        log.info("Sending SMS to: {}, from: {}, message: {}",
                 outGoingMessage.getTo(),
                 outGoingMessage.getFrom(),
                 outGoingMessage.getMessage());

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender(outGoingMessage.getTo());
        messageDTO.setMessage(outGoingMessage.getMessage());
        messageDTO.setPhoneNumber(outGoingMessage.getFrom());

        messageDTO.setStatus("Queued");

        smsMessageService.queueSendMessage(messageDTO);


        return "redirect:/"; // Redirect back to home after sending SMS
    }

}
