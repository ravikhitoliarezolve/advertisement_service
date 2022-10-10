package com.resolve.advertisement.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvertisementResponseDto extends Geo{
    private List<AdvertisementDto> advertisingModelList = new ArrayList<>();
}
