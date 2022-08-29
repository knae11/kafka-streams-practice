package com.deliverystreams.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeliveryEvent {
    private String id;
    private DeliveryState deliveryState;
    private LocalDateTime createdAt;
    private String deliveryDistrict;

    public DeliveryEvent(String id, DeliveryState deliveryState, LocalDateTime createdAt, String deliveryDistrict) {

    }
}
