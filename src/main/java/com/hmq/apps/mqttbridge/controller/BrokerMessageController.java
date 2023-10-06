package com.hmq.apps.mqttbridge.controller;

import com.hmq.apps.mqttbridge.service.MessageHandlingService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/mqtt")
public class BrokerMessageController {
    private final MessageHandlingService messageHandlingService;

    public BrokerMessageController(MessageHandlingService messageHandlingService) {
        this.messageHandlingService = messageHandlingService;
    }

    @PostMapping("{broker-name}/send/{topic-name}")
    public void sendMessage(@PathVariable(name = "broker-name") String brokerName,
                            @PathVariable(name = "topic-name") String topicName,
                            @RequestBody String message) {
        messageHandlingService.sendMessage(topicName, brokerName, message);
    }

    @GetMapping("{broker-name}/receive/{topic-name}")
    public Flux<String> readMessages(@PathVariable(name = "broker-name") String brokerName,
                                     @PathVariable(name = "topic-name") String topicName) {
        return messageHandlingService.readMessages(topicName, brokerName);
    }
}
