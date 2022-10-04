package com.resolve.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.resolve.advertisement.entity.AdvertisementEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties
@NoArgsConstructor
public class AdvertisementDto {
	private Long addId;
    private Double latitude;
    private Double longitude;
    private String href;
    private String advertisementName;
    private Double distance;

    public AdvertisementDto(AdvertisementEntity advertisementEntity) {
        this.addId = advertisementEntity.getAddId();
        this.latitude = advertisementEntity.getLatitude();
        this.longitude = advertisementEntity.getLongitude();
        this.href = advertisementEntity.getHref();
        this.advertisementName = advertisementEntity.getAdvertisementName();
        this.distance = 0.0;
}
}
