package com.hmq.apps.mqttbridge.controller;

import com.hmq.apps.mqttbridge.exception.EntityNotFound;
import com.hmq.apps.mqttbridge.model.BrokerConfigDto;
import com.hmq.apps.mqttbridge.service.BrokerConfigService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureWebTestClient
class BrokerConfigurationControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BrokerConfigService brokerConfigService;

    @BeforeEach
    void setUp() {
        brokerConfigService.deleteAllBrokerConfigurations();
    }

    @Test
    void createBrokerConfig_dataProvided_successful() {
        // ARRANGE
        String brokerName = "broker-1";
        String brokerHostName = "some-host";
        int brokerPort = 1883;
        BrokerConfigDto brokerConfigDto = sampleBrokerConfigDto(brokerHostName, brokerPort);

        // ACT and ASSERT
        webTestClient.put()
                .uri("/mqtt/" + brokerName)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(brokerConfigDto), BrokerConfigDto.class)
                .exchange()
                .expectStatus().isOk();

        BrokerConfigDto brokerConfiguration = brokerConfigService.getBrokerConfiguration(brokerName).block();
        assertEquals(brokerConfiguration.getBrokerPort(), brokerPort);
        assertEquals(brokerConfiguration.getBrokerHostName(), brokerHostName);
    }

    @Test
    void createBrokerConfig_alreadyExistingBroker_fail() {
        // ARRANGE
        String brokerName = "broker-1";
        String brokerHostName = "some-host";
        int brokerPort = 1883;
        BrokerConfigDto brokerConfigDto = sampleBrokerConfigDto(brokerHostName, brokerPort);
        // ARRANGE: create broker config
        brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto);

        // ACT and ASSERT
        webTestClient.put()
                .uri("/mqtt/" + brokerName)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(brokerConfigDto), BrokerConfigDto.class)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void getBrokerConfig_brokerDefined_successful() {
        // ARRANGE
        String brokerName = "broker-1";
        String brokerHostName = "some-host";
        int brokerPort = 1883;
        BrokerConfigDto brokerConfigDto = sampleBrokerConfigDto(brokerHostName, brokerPort);
        // ARRANGE: create broker config
        brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto);

        // ACT and ASSERT
        webTestClient.get()
                .uri("/mqtt/" + brokerName)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BrokerConfigDto.class)
                .value(brokerConfig -> {
                    assertEquals(brokerConfig.getBrokerHostName(), brokerHostName);
                    assertEquals(brokerConfig.getBrokerPort(), brokerPort);
                });
    }

    @Test
    void getBrokerConfig_brokerNotDefined_fail() {
        // ARRANGE
        String brokerName = "broker-1";

        // ACT and ASSERT
        webTestClient.get()
                .uri("/mqtt/" + brokerName)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBrokerConfig_brokerDefined_successful() {
        // ARRANGE
        String brokerName = "broker-1";
        String brokerHostName = "some-host";
        int brokerPort = 1883;
        BrokerConfigDto brokerConfigDto = sampleBrokerConfigDto(brokerHostName, brokerPort);
        // ARRANGE: create broker config
        brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto);

        // ACT and ASSERT
        webTestClient.delete()
                .uri("/mqtt/" + brokerName)
                .exchange()
                .expectStatus().isOk();

        assertThrows(EntityNotFound.class, () -> brokerConfigService.getBrokerConfiguration(brokerName).block());
    }

    @NotNull
    private BrokerConfigDto sampleBrokerConfigDto(String brokerHostName, int brokerPort) {
        BrokerConfigDto brokerConfigDto = new BrokerConfigDto();
        brokerConfigDto.setBrokerPort(brokerPort);
        brokerConfigDto.setBrokerHostName(brokerHostName);
        return brokerConfigDto;
    }


}