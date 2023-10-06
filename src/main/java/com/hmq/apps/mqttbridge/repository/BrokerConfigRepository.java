package com.hmq.apps.mqttbridge.repository;

import com.hmq.apps.mqttbridge.domain.BrokerConfig;
import reactor.core.publisher.Mono;

public interface BrokerConfigRepository {
    Mono<BrokerConfig> create(String brokerName, BrokerConfig brokerConfig);
    Mono<BrokerConfig> read(String brokerName);
    void delete(String brokerName);
    void deleteAll();
}
