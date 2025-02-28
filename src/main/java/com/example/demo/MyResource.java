package com.example.demo;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.function.Consumer;

@RestController
public class MyResource {

    public static final String BINDING_NAME = "test";
    private StreamBridge streamBridge;

    public MyResource(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/")
    public String index() {
        var uuid = UUID.randomUUID().toString();
        var SpringCloudStreamSuccess = streamBridge.send(BINDING_NAME, MessageBuilder.withPayload(uuid).build());

        if (SpringCloudStreamSuccess) {
            return "Success: " + uuid;
        } else {
            return "Failure: " + uuid;
        }
    }

    @Bean
    public Consumer<ErrorMessage> myErrorHandler() {
        return v -> {
            System.out.println("An error occurred with the data sending:" + v);
        };
    }
}
