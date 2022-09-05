package com.deliverystreams.application.serde;

import com.deliverystreams.domain.DeliveryEvent;
import org.springframework.kafka.support.serializer.JsonSerde;

public class DeliveryEventSerde extends JsonSerde<DeliveryEvent> {
}
