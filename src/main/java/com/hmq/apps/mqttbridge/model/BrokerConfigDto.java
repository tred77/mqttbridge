package com.hmq.apps.mqttbridge.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerConfigDto {
    private String brokerHostName;
    private int brokerPort;
}
