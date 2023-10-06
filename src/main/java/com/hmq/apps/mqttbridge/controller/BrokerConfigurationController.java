package com.hmq.apps.mqttbridge.controller;

import com.hmq.apps.mqttbridge.model.BrokerConfigDto;
import com.hmq.apps.mqttbridge.service.BrokerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mqtt")
public class BrokerConfigurationController {

    private final BrokerConfigService brokerConfigService;

    @Autowired
    public BrokerConfigurationController(BrokerConfigService brokerConfigService) {
        this.brokerConfigService = brokerConfigService;
    }

    @PutMapping("/{broker-name}")
    public Mono<Void> createBrokerConfiguration(@PathVariable("broker-name") String brokerName, @RequestBody BrokerConfigDto brokerConfigDto) {
        return Mono.fromRunnable(() -> brokerConfigService.createBrokerConfiguration(brokerName, brokerConfigDto));
    }

    @GetMapping("/{broker-name}")
    public Mono<BrokerConfigDto> getBrokerConfiguration(@PathVariable("broker-name") String brokerName) {
        return brokerConfigService.getBrokerConfiguration(brokerName);
    }

    @DeleteMapping("/{broker-name}")
    public Mono<Void> deleteBrokerConfiguration(@PathVariable("broker-name") String brokerName) {
        return Mono.fromRunnable(() -> brokerConfigService.deleteBrokerConfiguration(brokerName));
    }
}