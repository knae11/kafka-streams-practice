package com.deliverystreams.application;

import com.deliverystreams.domain.DeliveryState;
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
        Optional<ReadOnlyKeyValueStore<DistrictDeliveryStateCondition, Long>> countPerStateStore = deliveryAggregatorConfiguration.getCountPerStateStore();
        if (countPerStateStore.isEmpty()) {
            return null;
        }

        return countPerStateStore.get().get(DistrictDeliveryStateCondition.of(localDate, deliveryDistrict, deliveryState));
    }
}
