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
- [x] 지역별 각 배달 상태의 배달 갯수 조회

---
[Spring cloud stream binder kafka](https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/3.2.1/reference/html/spring-cloud-stream-binder-kafka.html#_programming_model)

### Functional Style
- Bean 네임을 따라 `{beanName}-in-0` 형식으로 바인딩
- key, value Serde 를 지정하지 않으면 기본 값으로 직렬화/역직렬화를 진행
- input: destination으로 지정된 topic으로 들어오는 이벤트
- Materialize.as 를 통해 StateStore에 구체화됨
  ```java
    public static <K, V, S extends StateStore> Materialized<K, V, S> as(final String storeName) {
        Named.validate(storeName);
        return new Materialized<>(storeName);
    }
  ``` 
- KTable: (key, value). unique key로 가장 최신 레코드를 사용. 동일한 key 값에 대해 value는 가장 마지막 값의 update로 해석
- KStream: (key, value). 레코드의 흐름. key가 같더라도 insert로 해석(대체 x)
- KGroupedStream: (key, value) 그룹화된 레코드 스트림의 추상화. KStream 레코드에 집계작업을 적용하기 위한 KStream의 중간 표현.

- interactiveQuery: 대화형 쿼리기능. KTable의 값을 조회할 수 있음. 