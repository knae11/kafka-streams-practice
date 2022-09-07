package com.deliverystreams.adapter;

import com.deliverystreams.application.DeliveryFinder;
import com.deliverystreams.domain.DeliveryEvent;
import com.deliverystreams.domain.DeliveryState;
import com.deliverystreams.domain.District;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/v1/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final KafkaTemplate<String, DeliveryEvent> kafkaTemplate;
    private final DeliveryFinder deliveryFinder;

    @PostMapping("/init")
    public void initialize() throws Exception {
        log.info("초기 셋팅");
        final LocalDateTime now = LocalDateTime.now();
        // 111 지역 10개
        for (int i = 0; i < 10; i++) {
            ActionRequest request = new ActionRequest(String.valueOf(i), "WAIT_ALLOCATE", "SONG_PA");
            DeliveryEvent event = new DeliveryEvent(request.getId(), DeliveryState.valueOf(request.getDeliveryState()), now.minusSeconds(i), District.valueOf(request.getDeliveryDistrict()));
            kafkaTemplate.send("delivery", event.getId(), event).get();
        }

        // 112 지역 7개
        for (int i = 10; i < 17; i++) {
            ActionRequest request = new ActionRequest(String.valueOf(i), "WAIT_ALLOCATE", "GANG_NAM");
            DeliveryEvent event = new DeliveryEvent(request.getId(), DeliveryState.valueOf(request.getDeliveryState()), now.minusSeconds(i), District.valueOf(request.getDeliveryDistrict()));
            kafkaTemplate.send("delivery", event.getId(), event).get();
        }

        // 113 지역 3개
        for (int i = 17; i < 20; i++) {
            ActionRequest request = new ActionRequest(String.valueOf(i), "WAIT_ALLOCATE", "GANG_DONG");
            DeliveryEvent event = new DeliveryEvent(request.getId(), DeliveryState.valueOf(request.getDeliveryState()), now.minusSeconds(i), District.valueOf(request.getDeliveryDistrict()));
            kafkaTemplate.send("delivery", event.getId(), event).get();
        }
        log.info("초기 셋팅 완료");
    }

    @PostMapping("/action")
    public void waitAllocate(@RequestBody ActionRequest request) throws ExecutionException, InterruptedException {
        DeliveryEvent event = new DeliveryEvent(request.getId(), DeliveryState.valueOf(request.getDeliveryState()), LocalDateTime.now(), District.valueOf(request.getDeliveryDistrict()));
        kafkaTemplate.send("delivery", event.getId(), event).get();
    }

    @GetMapping("/count")
    public Long getCount(@RequestParam LocalDate localDate, @RequestParam District deliveryDistrict, @RequestParam DeliveryState deliveryState) {
        return deliveryFinder.getCount(localDate, deliveryDistrict, deliveryState);
    }

    @GetMapping("/count/state")
    public Long getCountState(@RequestParam LocalDate localDate, @RequestParam DeliveryState deliveryState) {
        return deliveryFinder.getCount(localDate, deliveryState);
    }

    @NoArgsConstructor
    @Getter
    static class ActionRequest {
        private String id;
        private String deliveryState;
        private String deliveryDistrict;

        public ActionRequest(String id, String deliveryState, String deliveryDistrict) {
            this.id = id;
            this.deliveryState = deliveryState;
            this.deliveryDistrict = deliveryDistrict;
        }
    }

}
