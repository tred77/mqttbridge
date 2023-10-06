package com.hmq.apps.mqttbridge;

import com.hmq.apps.mqttbridge.controller.BrokerMessageController;
import com.hmq.apps.mqttbridge.model.BrokerConfigDto;
import com.hmq.apps.mqttbridge.service.BrokerConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public class SendAndReceiveMessageIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BrokerConfigService brokerConfigService;
    @Autowired
    private BrokerMessageController brokerMessageController;
    @Container
    public GenericContainer<?> hivemq = new GenericContainer<>("hivemq/hivemq4")
            .withExposedPorts(1883);

    @BeforeEach
    void setUp() {
        brokerConfigService.deleteAllBrokerConfigurations();
    }

    @Test
    void sendMessage_topicIsDefined_successful() throws Exception {
        // ARRANGE
        int mappedPort = hivemq.getMappedPort(1883);
        String brokerName = "broker-1";
        String topicName = "some-topic";
        BrokerConfigDto brokerConfigDto = new BrokerConfigDto();
        brokerConfigDto.setBrokerHostName("localhost");
        brokerConfigDto.setBrokerPort(mappedPort);
        brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto);

        // ACT and ASSERT
        webTestClient.post()
                .uri("/mqtt/" + brokerName + "/send/" + topicName)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("some-message"), String.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void receiveMessage_brokerDefined_receiveMessagesSuccessfully() throws Exception {
        // ARRANGE
        int mappedPort = hivemq.getMappedPort(1883);
        String brokerName = "broker-1";
        String topicName = "send-receive-topic";
        BrokerConfigDto brokerConfigDto = new BrokerConfigDto();
        brokerConfigDto.setBrokerHostName("localhost");
        brokerConfigDto.setBrokerPort(mappedPort);
        brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto);

        // ACT: subscribe to the broker
        Flux<String> resultMessagesFlux = brokerMessageController.readMessages(brokerName, topicName);
        // ACT: send a message to the broker
        sendMessageToBroker("some-test-message", brokerName, topicName);

        // ASSERT
        StepVerifier.create(resultMessagesFlux)
                .expectNext("some-test-message")
                .thenCancel()
                .verify();
    }

    private void sendMessageToBroker(String message, String brokerName, String topicName){
        webTestClient.post()
                .uri("/mqtt/" + brokerName + "/send/" + topicName)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(message), String.class)
                .exchange();
    }

}
