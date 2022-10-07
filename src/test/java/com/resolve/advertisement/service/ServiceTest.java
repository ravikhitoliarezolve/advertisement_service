package com.resolve.advertisement.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.advertisement.AppConstantTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolve.advertisement.dto.GeoDto;
import com.resolve.advertisement.dto.ResponseDto;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.entity.AdvertisementGeoMapping;
import com.resolve.advertisement.repository.AdvertisementGeoMappingRepository;
import com.resolve.advertisement.repository.AdvertisementRepository;
import com.resolve.advertisement.utils.HaversineDistanceCalculator;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
	
	@InjectMocks
	AdvertisementService advertisementService;
	
	@Mock 
	AdvertisementRepository advertisementRepository;
	@Mock
	AdvertisementGeoMappingRepository advertisGeoMappingRepository;
	AdvertisementEntity advertisementEntity = new AdvertisementEntity();
	
	@Mock
	RestTemplate restTemplate;
	
	@Mock
	HaversineDistanceCalculator haversineDistanceCalculator;
	List<AdvertisementEntity>  AdvertisementEntity=new ArrayList<>();
	List<AdvertisementGeoMapping> AdvertisementGeoMappings= new ArrayList<>();
	
	@BeforeEach
    public void setup() {
		AdvertisementEntity advertisementEntity = new AdvertisementEntity();
		advertisementEntity.setAddId(121L);
		advertisementEntity.setHref("https://www.facebook.com");
		advertisementEntity.setLongitude(14.0);
		advertisementEntity.setLatitude(15.0);
		advertisementEntity.setAdvertisementName("Test");
		advertisementEntity.setUpdatedAt(new Date());
		advertisementEntity.setCreatedAt(new Date());
    }

	
	@Test
    void addAdvertisement() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        when(restTemplate.exchange(advertisementEntity.getHref(), HttpMethod.GET, entity, String.class)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        given(advertisementRepository.save(Mockito.any())).willReturn(advertisementEntity);
        GeoDto geoDto = mapper.readValue(AppConstantTest.GEO_RESPONSE, GeoDto.class);
        when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(ResponseEntity.ok(geoDto));
        given(haversineDistanceCalculator.checkInside(Mockito.any(),Mockito.any(),Mockito.any())).willReturn(true);
        ResponseDto advertisingModelResponse = advertisementService.addAdvertisement(advertisementEntity);
        Assertions.assertEquals(200, advertisingModelResponse.getCode());
    }
	
	@Test
    void advertisementEntity_invalidUrl() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        when(restTemplate.exchange(advertisementEntity.getHref(), HttpMethod.GET, entity, String.class)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        ResponseDto advertisingModelResponse = advertisementService.addAdvertisement(advertisementEntity);
        Assertions.assertEquals(424, advertisingModelResponse.getCode());
    }
	@Test
    void updateAdvertisement() {
		advertisementEntity.setLongitude(11.23);
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.of(advertisementEntity));
        given(advertisementRepository.save(Mockito.any())).willReturn(advertisementEntity);
        ResponseDto advertisingModelResponse = advertisementService.updateAdvertisement(advertisementEntity);
        Assertions.assertEquals(200, advertisingModelResponse.getCode());
    }
	
	@Test
    void updateAdvertisement_not_found() {
		advertisementEntity.setAddId(201L);
		advertisementEntity.setLongitude(11.23);
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.empty());
        ResponseDto advertisingModelResponse = advertisementService.updateAdvertisement(advertisementEntity);
        Assertions.assertEquals(404, advertisingModelResponse.getCode());
    }
	
	@Test
    void deleteAdvertisement() {
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.of(advertisementEntity));
        ResponseDto advertisingModelResponse = advertisementService.deleteAdvertisement(advertisementEntity.getAddId());
        Assertions.assertEquals(200, advertisingModelResponse.getCode());
    }

    @Test
    void deleteAdvertisement_not_found() {
        given(advertisementRepository.findById(Mockito.any())).willReturn(Optional.empty());
        ResponseDto advertisingModelResponse = advertisementService.deleteAdvertisement(advertisementEntity.getAddId());
        Assertions.assertEquals(404, advertisingModelResponse.getCode());
    }

}


