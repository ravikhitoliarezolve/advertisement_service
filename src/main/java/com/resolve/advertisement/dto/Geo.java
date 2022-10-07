package com.resolve.advertisement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Geo {
	
    private Long Id;
    private Double latitude;
    private Double longitude;
    private Double radius;

}
