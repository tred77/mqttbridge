package com.hmq.apps.mqttbridge.service.internal;

import com.hmq.apps.mqttbridge.domain.BrokerConfig;
import com.hmq.apps.mqttbridge.model.BrokerConfigDto;
import com.hmq.apps.mqttbridge.repository.BrokerConfigRepository;
import com.hmq.apps.mqttbridge.service.BrokerConfigService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
class DefaultBrokerConfigService implements BrokerConfigService {
    private final BrokerConfigRepository brokerConfigRepository;

    DefaultBrokerConfigService(BrokerConfigRepository brokerConfigRepository) {
        this.brokerConfigRepository = brokerConfigRepository;
    }
    @Override
    public void createBrokerConfiguration(String brokerName, BrokerConfigDto brokerConfigDto) {
        brokerConfigRepository.create(brokerName, toDomain(brokerConfigDto));
    }

    @Override
    public Mono<BrokerConfigDto> getBrokerConfiguration(String brokerName) {
        return brokerConfigRepository.read(brokerName)
                .map(this::toDto);
    }

    @Override
    public void deleteBrokerConfiguration(String brokerName) {
        brokerConfigRepository.delete(brokerName);

    }

    @Override
    public void deleteAllBrokerConfigurations() {
        brokerConfigRepository.deleteAll();
    }

    private BrokerConfigDto toDto(BrokerConfig brokerConfig) {
        BrokerConfigDto dto = new BrokerConfigDto();
        dto.setBrokerHostName(brokerConfig.getBrokerHostName());
        dto.setBrokerPort(brokerConfig.getBrokerPort());
        return dto;
    }

    private BrokerConfig toDomain(BrokerConfigDto brokerConfigDto) {
        BrokerConfig domain = new BrokerConfig();
        domain.setBrokerHostName(brokerConfigDto.getBrokerHostName());
        domain.setBrokerPort(brokerConfigDto.getBrokerPort());
        return domain;
    }
}
