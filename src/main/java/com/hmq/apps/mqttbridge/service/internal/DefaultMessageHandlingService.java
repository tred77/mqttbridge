package com.hmq.apps.mqttbridge.service.internal;

import com.hmq.apps.mqttbridge.agent.MqttPublisher;
import com.hmq.apps.mqttbridge.agent.MqttSubscriber;
import com.hmq.apps.mqttbridge.repository.BrokerConfigRepository;
import com.hmq.apps.mqttbridge.service.MessageHandlingService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
class DefaultMessageHandlingService implements MessageHandlingService {
    private final BrokerConfigRepository brokerConfigRepository;
    private final MqttPublisher mqttPublisher;
    private final MqttSubscriber mqttSubscriber;

    DefaultMessageHandlingService(BrokerConfigRepository brokerConfigRepository, MqttPublisher mqttPublisher, MqttSubscriber mqttSubscriber) {
        this.brokerConfigRepository = brokerConfigRepository;
        this.mqttPublisher = mqttPublisher;
        this.mqttSubscriber = mqttSubscriber;
    }

    @Override
    public void sendMessage(String topic, String brokerName, String message) {
        brokerConfigRepository.read(brokerName)
                        .subscribe(config -> mqttPublisher.sendMessage(config, topic, message));
    }

    @Override
    public Flux<String> readMessages(String topic, String brokerName) {
        return brokerConfigRepository.read(brokerName)
                .flatMapMany(config -> mqttSubscriber.subscribe(topic, config));
    }
}
