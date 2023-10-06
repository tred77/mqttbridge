package com.hmq.apps.mqttbridge.controller;

import com.hmq.apps.mqttbridge.model.BrokerConfigDto;
import com.hmq.apps.mqttbridge.service.BrokerConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
class BrokerMessageControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BrokerConfigService brokerConfigService;

    @BeforeEach
    void setUp() {
        brokerConfigService.deleteAllBrokerConfigurations();
    }

    @Test
    void sendMessage_brokerNotDefined_failWithError() {
        // ARRANGE
        // no broker defined yet - so this should fail

        // ACT and ASSERT
        webTestClient.post()
                .uri("/mqtt/" + "undefined-broker" + "/send/" + "some-topic")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("some-message"), String.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void sendMessage_noMessageProvided_failWithError() {
        // ARRANGE
        String brokerName = "broker-1";
        String topicName = "some-topic";
        BrokerConfigDto brokerConfigDto = new BrokerConfigDto();
        brokerConfigDto.setBrokerHostName("localhost");
        brokerConfigDto.setBrokerPort(1883);
        brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto);

        // ACT and ASSERT
        webTestClient.post()
                .uri("/mqtt/" + "undefined-broker" + "/send/" + "some-topic")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void receiveMessage_brokerNotDefined_failWithError() {
        // ARRANGE
        // no broker defined yet - so this should fail

        // ACT and ASSERT
        webTestClient.get()
                .uri("/mqtt/" + "undefined-broker" + "/receive/" + "some-topic")
                .exchange()
                .expectStatus().isNotFound();
    }
}