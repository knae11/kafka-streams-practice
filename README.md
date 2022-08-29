# Kafka Streams 실습

- 지역별로 배달을 생성 및 상태 조건을 설정한다.
```text
DeliveryState: 
    WAIT_ALLOCATE, 
    COMPLETE_ALLOCATE,
    COMPLETE_PICKUP,
    COMPLETE_DELIVERY
    
District:
    - 송파(111)
    - 강남(112)
    - 강동(113)
```
- [ ] 지역별 배차대기(WAIT_ALLOCATE) 상태의 배달 갯수 조회
- [ ] 지역별 배달되지 않은(COMPLETE_DELIVERY가 아닌) 배달 갯수 조회
