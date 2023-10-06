package com.hmq.apps.mqttbridge.service;

import com.hmq.apps.mqttbridge.model.BrokerConfigDto;
import reactor.core.publisher.Mono;

public interface BrokerConfigService {

    void createBrokerConfiguration(String brokerName, BrokerConfigDto brokerConfigDto);

    Mono<BrokerConfigDto> getBrokerConfiguration(String brokerName);

    void deleteBrokerConfiguration(String brokerName);

    void deleteAllBrokerConfigurations();
}
