package com.deliverystreams.application;

import com.deliverystreams.domain.DeliveryState;
import com.deliverystreams.domain.DeliveryStateCondition;
import com.deliverystreams.domain.District;
import com.deliverystreams.domain.DistrictDeliveryStateCondition;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryFinder {
    private final DeliveryAggregatorConfiguration deliveryAggregatorConfiguration;

    public Long getCount(LocalDate localDate, District deliveryDistrict, DeliveryState deliveryState) {
        Optional<ReadOnlyKeyValueStore<DistrictDeliveryStateCondition, Long>> countPerStateStore = deliveryAggregatorConfiguration.getCountPerDistrict();
        if (countPerStateStore.isEmpty()) {
            return null;
        }

        return countPerStateStore.get().get(DistrictDeliveryStateCondition.of(localDate, deliveryDistrict, deliveryState));
    }

    public Long getCount(LocalDate localDate,DeliveryState deliveryState) {
        Optional<ReadOnlyKeyValueStore<DeliveryStateCondition, Long>> countPerStateStore = deliveryAggregatorConfiguration.getCountPerState();
        if (countPerStateStore.isEmpty()) {
            return null;
        }

        return countPerStateStore.get().get(DeliveryStateCondition.of(localDate,  deliveryState));
    }
}
