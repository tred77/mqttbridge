package com.hmq.apps.mqttbridge.service;

import reactor.core.publisher.Flux;

public interface MessageHandlingService {
    void sendMessage(String topic, String brokerName, String message);
    Flux<String> readMessages(String topic, String brokerName);
}
