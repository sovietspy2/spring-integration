package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class Config {
        public static final String INBOUND_CHANNEL = "inbound-channel";

        @Bean(name = INBOUND_CHANNEL)
        public MessageChannel inboundFilePollingChannel() {
            return MessageChannels.direct().get();
        }

}
