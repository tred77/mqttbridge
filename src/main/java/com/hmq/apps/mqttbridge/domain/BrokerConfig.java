package com.hmq.apps.mqttbridge.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BrokerConfig {
    private String brokerHostName;
    private int brokerPort;
}
