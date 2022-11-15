package com.deliverystreams.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
public class DeliveryStateCondition {
    private LocalDate localDate;
    private DeliveryState deliveryState;

    public DeliveryStateCondition(LocalDate localDate, DeliveryState deliveryState) {
        this.localDate = localDate;
        this.deliveryState = deliveryState;
    }

    public static DeliveryStateCondition of(LocalDate localDate, DeliveryState deliveryState) {
        return new DeliveryStateCondition(localDate, deliveryState);
    }
}
