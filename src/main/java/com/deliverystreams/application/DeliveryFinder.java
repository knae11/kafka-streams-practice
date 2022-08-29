package com.deliverystreams.application;

import com.deliverystreams.domain.DeliveryState;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Service
public class DeliveryFinder {
    public Map<Object, Object> getWaitAllocateWindowCount(LocalDate localDate, String deliveryDistrict, DeliveryState deliveryState, LocalTime fromTime, LocalTime toTime) {
        throw new UnsupportedOperationException();
    }

    public Long getCount(LocalDate localDate, String deliveryDistrict, DeliveryState deliveryState) {
        throw new UnsupportedOperationException();
    }
}
