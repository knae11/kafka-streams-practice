# Kafka Streams 실습

- 지역별로 배달을 생성 및 상태 조건을 설정한다.
```text
DeliveryState: 
    WAIT_ALLOCATE, 
    COMPLETE_ALLOCATE,
    COMPLETE_PICKUP,
    COMPLETE_DELIVERY
    
District:
    - SONG_PA(111)
    - GANG_NAM(112)
    - GANG_DONG(113)
```
- 초기 셋팅은 배차대기 상태로 송파10, 강남7, 강동3 개의 배달을 만든다.

---
- [x] 지역별 각 배달 상태의 배달 갯수 조회
