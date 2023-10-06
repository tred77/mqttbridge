package com.hmq.apps.mqttbridge.repository.internal;

import com.hmq.apps.mqttbridge.domain.BrokerConfig;
import com.hmq.apps.mqttbridge.exception.DuplicatedEntityException;
import com.hmq.apps.mqttbridge.exception.EntityNotFound;
import com.hmq.apps.mqttbridge.repository.BrokerConfigRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryBrokerConfigRepository implements BrokerConfigRepository {
    private static final Map<String, BrokerConfig> BROKER_CONFIGS = new ConcurrentHashMap<>();

    @Override
    public Mono<BrokerConfig> create(String brokerName, BrokerConfig brokerConfig) {
        if (BROKER_CONFIGS.get(brokerName) != null)
            throw new DuplicatedEntityException("Broker config already existing");
        BROKER_CONFIGS.put(brokerName, brokerConfig);
        return Mono.just(brokerConfig);
    }

    @Override
    public Mono<BrokerConfig> read(String brokerName) {
        if(BROKER_CONFIGS.get(brokerName) == null)
            throw new EntityNotFound("Broker config with name " + brokerName + " not found");
        return Mono.just(BROKER_CONFIGS.get(brokerName));
    }

    @Override
    public void delete(String brokerName) {
        if(BROKER_CONFIGS.get(brokerName) == null)
            throw new EntityNotFound("Broker config with name " + brokerName + " not found");
        BROKER_CONFIGS.remove(brokerName);
    }

    @Override
    public void deleteAll() {
        BROKER_CONFIGS.clear();
    }
}
