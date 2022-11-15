package com.deliverystreams.application;

import com.deliverystreams.application.serde.DeliveryEventSerde;
import com.deliverystreams.domain.DeliveryEvent;
import com.deliverystreams.domain.DeliveryStateCondition;
import com.deliverystreams.domain.DistrictDeliveryStateCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeliveryAggregatorConfiguration {
    private static final String STORE_LATEST_DELIVERY = "store-latest-delivery";
    private static final String STORE_COUNT_PER_DELIVERY_DISTRICT = "store-count-per-delivery-district";
    private static final String STORE_COUNT_PER_DELIVERY_STATE = "store-count-per-delivery-state";
    private static final String KTABLE_TO_PRINT = "delivery-ktable-to-print";

    private final JsonSerde<DeliveryEvent> deliveryEventSerde = new DeliveryEventSerde();
    private static final JsonSerde<DistrictDeliveryStateCondition> districtDeliveryStateConditionSerde = new JsonSerde<>(DistrictDeliveryStateCondition.class);
    private static final JsonSerde<DeliveryStateCondition> deliveryStateConditionSerde = new JsonSerde<>(DeliveryStateCondition.class);

    private final InteractiveQueryService interactiveQueryService;

    private final StreamsBuilder builder = new StreamsBuilder();

    @Bean
    public Consumer<KStream<String, DeliveryEvent>> deliveryCountAggregator() {
        return input -> {

            input.print(Printed.<String, DeliveryEvent>toSysOut()
                    .withLabel("DELIVERY-KStream"));
            // https://stackoverflow.com/questions/42937057/kafka-streams-api-kstream-to-ktable
            KTable<String, DeliveryEvent> table = input.groupByKey()
                    .reduce(new Reducer<DeliveryEvent>() {
                        @Override
                        public DeliveryEvent apply(DeliveryEvent value1, DeliveryEvent value2) {
                            return value2;
                        }
                    }, Materialized.as(KTABLE_TO_PRINT));
            table.toStream()
                    .print(Printed.<String, DeliveryEvent>toSysOut()
                            .withLabel("DELIVERY-KTable"));

            KTable<String, DeliveryEvent> latestDeliveryEvent = input.groupByKey(Grouped.with(Serdes.String(), deliveryEventSerde))
                    .reduce(DeliveryAggregatorConfiguration::latest, Materialized.<String, DeliveryEvent, KeyValueStore<Bytes, byte[]>>as(STORE_LATEST_DELIVERY)
                            .withKeySerde(Serdes.String())
                            .withValueSerde(deliveryEventSerde));

            latestDeliveryEvent.groupBy(((key, value) -> KeyValue.pair(
                                    DistrictDeliveryStateCondition.of(
                                            value.getOccurredDateTime().toLocalDate(),
                                            value.getDeliveryDistrict(),
                                            value.getDeliveryState()
                                    ), value.getId())),
                            Grouped.with(districtDeliveryStateConditionSerde, Serdes.String()
                            ))
                    .count(Materialized.<DistrictDeliveryStateCondition, Long, KeyValueStore<Bytes, byte[]>>as(STORE_COUNT_PER_DELIVERY_DISTRICT)
                            .withKeySerde(districtDeliveryStateConditionSerde)
                            .withValueSerde(Serdes.Long()));

            latestDeliveryEvent.groupBy(((key, value) -> KeyValue.pair(
                                    DeliveryStateCondition.of(
                                            value.getOccurredDateTime().toLocalDate(),
                                            value.getDeliveryState()
                                    ), value.getId())),
                            Grouped.with(deliveryStateConditionSerde, Serdes.String()
                            ))
                    .count(Materialized.<DeliveryStateCondition, Long, KeyValueStore<Bytes, byte[]>>as(STORE_COUNT_PER_DELIVERY_STATE)
                            .withKeySerde(deliveryStateConditionSerde)
                            .withValueSerde(Serdes.Long()));
        };
    }

    private static DeliveryEvent latest(DeliveryEvent oldest, DeliveryEvent newly) {
        if (oldest == null) {
            return newly;
        }
        if (newly == null) {
            return oldest;
        }

        return oldest.getOccurredDateTime().isBefore(newly.getOccurredDateTime()) ? newly : oldest;

    }

    public Optional<ReadOnlyKeyValueStore<DistrictDeliveryStateCondition, Long>> getCountPerDistrict() {
        try {
            return Optional.of(interactiveQueryService.getQueryableStore(STORE_COUNT_PER_DELIVERY_DISTRICT, QueryableStoreTypes.keyValueStore()));
        } catch (Exception e) {
            /**
             * StreamProcessor가 실행되는 동안(재배포 또는 재시작)에는 StateStore를 조회할 수 없다.
             */
            log.warn("State Store를 찾을 수 없습니다.", e);
            return Optional.empty();
        }
    }

    public Optional<ReadOnlyKeyValueStore<DeliveryStateCondition, Long>> getCountPerState() {
        try {
            return Optional.of(interactiveQueryService.getQueryableStore(STORE_COUNT_PER_DELIVERY_STATE, QueryableStoreTypes.keyValueStore()));
        } catch (Exception e) {
            /**
             * StreamProcessor가 실행되는 동안(재배포 또는 재시작)에는 StateStore를 조회할 수 없다.
             */
            log.warn("State Store를 찾을 수 없습니다.", e);
            return Optional.empty();
        }
    }
}
