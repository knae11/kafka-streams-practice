package com.deliverystreams.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class DeliveryEvent {
    private String id;
    private DeliveryState deliveryState;
    private LocalDateTime occurredDateTime;
    private District deliveryDistrict;

    public DeliveryEvent(String id, DeliveryState deliveryState, LocalDateTime occurredDateTime, District deliveryDistrict) {
        this.id = id;
        this.deliveryDistrict = deliveryDistrict;
        this.occurredDateTime = occurredDateTime;
        this.deliveryState = deliveryState;
    }
}
