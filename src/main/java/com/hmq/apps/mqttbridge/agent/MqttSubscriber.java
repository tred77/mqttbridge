package com.hmq.apps.mqttbridge.agent;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import com.hmq.apps.mqttbridge.domain.BrokerConfig;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class MqttSubscriber {
    public Flux<String> subscribe(String topic, BrokerConfig brokerConfig) {
        Mqtt5RxClient reactiveClient = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(brokerConfig.getBrokerHostName())
                .serverPort(brokerConfig.getBrokerPort())
                .buildRx();

        Flux<String> flux = Flux.from(reactiveClient.publishes(MqttGlobalPublishFilter.ALL))
                .map(publish -> new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8));

        final Single<Mqtt5SubAck> subAckSingle = reactiveClient.subscribeWith()
                .topicFilter(topic)
                .applySubscribe()
                .doOnSuccess(mqtt5SubAck -> {
                    log.info("Successfully subscribed!");
                }).doOnError(throwable -> {
                    log.info("Error while subscribing!");
                    throwable.printStackTrace();
                });

        reactiveClient.connect()
                .doOnSuccess(connAck -> {
                    log.info("Successfully connected!");
                    subAckSingle.subscribe();
                }).doOnError(throwable -> {
                    log.error("Error while connecting!");
                    throwable.printStackTrace();
                }).subscribe();

        flux.doOnCancel(() -> {
            log.info("Canceling subscription");
            reactiveClient.disconnect();
        });
        flux.doOnError(throwable -> {
            log.error("Error while reading messages!");
            throwable.printStackTrace();
        });
        return flux;
    }
}
