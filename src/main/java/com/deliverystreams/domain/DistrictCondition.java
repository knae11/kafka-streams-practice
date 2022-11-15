package com.deliverystreams.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor
public class DistrictCondition {

    private LocalDate localDate;
    private District district;

    public DistrictCondition(LocalDate localDate, District district) {
        this.localDate = localDate;
        this.district = district;
    }

    public static DistrictCondition of(LocalDate localDate, District district) {
        return new DistrictCondition(localDate, district);
    }
}
