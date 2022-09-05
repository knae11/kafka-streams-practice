package com.deliverystreams.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DistrictDeliveryStateCondition {
    private LocalDate localDate;
    private District district;
    private DeliveryState deliveryState;

    public DistrictDeliveryStateCondition(LocalDate localDate, District district, DeliveryState deliveryState) {
        this.localDate = localDate;
        this.district = district;
        this.deliveryState = deliveryState;
    }
    public static DistrictDeliveryStateCondition of(LocalDate localDate, District district, DeliveryState deliveryState) {
        return new DistrictDeliveryStateCondition(localDate, district, deliveryState);
    }
}
