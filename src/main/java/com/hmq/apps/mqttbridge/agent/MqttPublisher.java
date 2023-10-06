package com.hmq.apps.mqttbridge.agent;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hmq.apps.mqttbridge.domain.BrokerConfig;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MqttPublisher {

    public void sendMessage(BrokerConfig brokerConfig, String topicName, String message) {
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(brokerConfig.getBrokerHostName())
                .serverPort(brokerConfig.getBrokerPort())
                .buildBlocking();

        client.connect();
        client.publishWith()
                .topic(topicName)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(message.getBytes())
                .retain(true)
                .send();
        client.disconnect();
    }
}
