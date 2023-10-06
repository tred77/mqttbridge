package com.hmq.apps.mqttbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestMqttbridgeApplication {

	public static void main(String[] args) {
		SpringApplication.from(MqttbridgeApplication::main).with(TestMqttbridgeApplication.class).run(args);
	}

}
