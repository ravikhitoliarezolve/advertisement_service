package com.resolve.advertisement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolve.advertisement.constants.ApplicationConstants;
import com.resolve.advertisement.dto.AdvertisementDto;
import com.resolve.advertisement.dto.AdvertisementResponseDto;
import com.resolve.advertisement.dto.ErrorResponseDto;
import com.resolve.advertisement.dto.Geo;
import com.resolve.advertisement.dto.GeoDto;
import com.resolve.advertisement.dto.ResponseDto;
import com.resolve.advertisement.dto.SuccessResponseDto;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.entity.AdvertisementGeoMapping;
import com.resolve.advertisement.repository.AdvertisementGeoMappingRepository;
import com.resolve.advertisement.repository.AdvertisementRepository;
import com.resolve.advertisement.utils.HaversineDistanceCalculator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdvertisementService {

	@Autowired
	private AdvertisementRepository advertisementRepository;
	@Autowired
	RestTemplate restTemplate;
	@Value("${client.geo.baseUrl}")
	private String geoClientUrl;
	@Autowired
	HaversineDistanceCalculator haversineDistanceCalculator;

	@Autowired
	AdvertisementGeoMappingRepository advertisementGeoMappingRepository;
	
	
	
	@Transactional
	public ResponseDto addAdvertisement(AdvertisementEntity advertisement) {
	    int isValidUrl = validateHrefUrl(advertisement.getHref());
	    if (isValidUrl != 200) {
	        return new ErrorResponseDto(ApplicationConstants.HTTP_RESPONSE_ERROR_CODE_HREF,
	                     "received unsuccessful status code " + isValidUrl +
	                             " from " + advertisement.getHref());
	    }
	    AdvertisementEntity responseDto = advertisementRepository.save(advertisement);
	    List<Geo> geoFences = (List<Geo>) getAdvertisement(advertisement.getLatitude(),
	                        advertisement.getLongitude()).getData();
	    Set<AdvertisementGeoMapping> advertisingGeoFenceMappingSet = new HashSet<>();
	    AdvertisementGeoMapping advertisingGeoFenceMapping;
	    if (geoFences != null && !geoFences.isEmpty()) {
	        for (Geo loopGeoFence : geoFences) {
	            advertisingGeoFenceMapping = new AdvertisementGeoMapping(loopGeoFence.getId(),
	                                            responseDto.getAddId());
	            advertisingGeoFenceMappingSet.add(advertisingGeoFenceMapping);
	        }
	        advertisementGeoMappingRepository.saveAll(advertisingGeoFenceMappingSet);
	    }
	    return new SuccessResponseDto(responseDto);
	}
//	@Transactional
//	public ResponseDto addAdvertisement(AdvertisementEntity advertisementEntity) {
//		int isValidUrl = validateHrefUrl(advertisementEntity.getHref());
//		if (isValidUrl != 200) {
//			return new ErrorResponseDto(ApplicationConstants.HTTP_RESPONSE_ERROR_CODE_HREF,
//					"received unsuccessful status code " + isValidUrl + " from " + advertisementEntity.getHref());
//		}
//		AdvertisementEntity responseDto = advertisementRepository.save(advertisementEntity);
//		List<Geo> geos = (List<Geo>) getAdvertis(advertisementEntity.getLatitude(), advertisementEntity.getLongitude())
//				.getData();
//		
//		Set<AdvertisementGeoMapping> advertisementGeoMappingSet = new HashSet<>();
//		AdvertisementGeoMapping advertisementGeoMapping;
//		if (geos != null && !geos.isEmpty()) {
//			for (Geo loopGeoFence : geos) {
//				advertisementGeoMapping = new AdvertisementGeoMapping(loopGeoFence.getId(), responseDto.getAddId());
//				advertisementGeoMappingSet.add(advertisementGeoMapping);
//			}
//			advertisementGeoMappingRepository.saveAll(advertisementGeoMappingSet);
//		}
//		return new SuccessResponseDto(responseDto);
//	}
//
//	/*
//	 * This function update the advertising data based on unique advertisments id if
//	 * found in system
//	 */
	public ResponseDto updateAdvertisement(AdvertisementEntity request) {
		return advertisementRepository.findById(request.getAddId()).isPresent()
				? new SuccessResponseDto(advertisementRepository.save(request))
				: new ErrorResponseDto(ApplicationConstants.NOT_FOUND, ApplicationConstants.NOT_FOUND_MSG);
	}

	public ResponseDto deleteAdvertisement(Long advertisingId) {
		if (advertisementRepository.findById(advertisingId).isPresent()) {
			advertisementRepository.deleteById(advertisingId);
			return new SuccessResponseDto(ApplicationConstants.HTTP_RESPONSE_SUCCESS_CODE);
		}
		return new ErrorResponseDto(ApplicationConstants.NOT_FOUND, ApplicationConstants.NOT_FOUND_MSG);
	}

	/*
	 * This function update the advertising data based on unique advertising id if
	 * found in system
	 */

	private List<Geo> checkInsideGeoFences(List<Geo> geoFencesList, Double latitude, Double longitude) {
		List<Geo> insideGeoList = new ArrayList<>();
		geoFencesList.stream().forEach((loop) -> {
			log.info("checking lat {} long {} inside Geo {}", latitude, longitude, loop);
			if (haversineDistanceCalculator.checkInside(loop, longitude, latitude)) {
				insideGeoList.add(loop);
			}

		});
		return insideGeoList;
	}

	/*
	 * This function return the list of all geofence available at client system and
	 * then checks the unique geolocation which are inside the radius
	 */
	public ResponseDto getAdvertisement(Double latitude, Double longitude) {
		List<Geo> geoFencesList = checkInsideGeoFences(getGeoList(), latitude, longitude);
		return new SuccessResponseDto(geoFencesList);
	}

	/*
	 * This function returns the list of all client geofence by using http call
	 */
	public List<Geo> getGeoList() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		ResponseEntity<Object> response = restTemplate.getForEntity(geoClientUrl + "/getGeos", Object.class);
		log.info("list of geofence is {}", response);
		Object obj = response.getBody();
		ObjectMapper mapper = new ObjectMapper();
		GeoDto geoDto = mapper.convertValue(obj, GeoDto.class);
		return geoDto.getData();

	}
	
	public ResponseDto getAdvertisementInsideGeo(Double latitude, Double longitude) {
        List<Geo> geoList = checkInsideGeoFences(getGeoList(), latitude, longitude);
        List<Long> geoIds = geoList.parallelStream().map(Geo::getId).collect(Collectors.toList());
        List<AdvertisementGeoMapping> advertisGeoMappings = advertisementGeoMappingRepository.findByGeoIds(geoIds);
        List<Long> adIds = advertisGeoMappings.parallelStream().map(AdvertisementGeoMapping::getAddId).collect(Collectors.toList());
        List<AdvertisementEntity> advertisEntities = advertisementRepository.findAllById(adIds);
        Map<Long, AdvertisementEntity> advertisEntityMap = advertisEntities.stream().collect(Collectors.toMap(AdvertisementEntity::getAddId, Function.identity()));
        Map<Long, Geo> geoMap = geoList.stream().collect(Collectors.toMap(Geo::getId, Function.identity()));
        AdvertisementResponseDto responseDto;
        Map<Long, AdvertisementResponseDto> response = new HashMap<>();
        AdvertisementDto advertisingDto;
        for (AdvertisementGeoMapping advertisGeoMapping : advertisGeoMappings) {
            
            AdvertisementEntity advertisingModel = advertisEntityMap.get(advertisGeoMapping.getAddId());
            Geo geoFence = geoMap.get(advertisGeoMapping.getGeoId());
            if (response.get(advertisGeoMapping.getGeoId()) == null) {
                responseDto = new AdvertisementResponseDto();
                responseDto.setId(geoFence.getId());
                responseDto.setLongitude(geoFence.getLongitude());
                responseDto.setLatitude(geoFence.getLatitude());
                responseDto.setRadius(geoFence.getRadius());
                advertisingDto = new AdvertisementDto(advertisingModel);
                double distance = haversineDistanceCalculator.haversineDistance(geoFence.getLongitude(), geoFence.getLatitude(), advertisingModel.getLongitude(), advertisingModel.getLatitude());
                advertisingDto.setDistance(distance);
                responseDto.getAdvertisingModelList()
                .add(advertisingDto);
                response.put(geoFence.getId(),responseDto);
                
            }
            else {
                AdvertisementResponseDto responseDto1 =  response.get(advertisGeoMapping.getGeoId());
                advertisingDto = new AdvertisementDto(advertisingModel);
                double distance = haversineDistanceCalculator.haversineDistance(geoFence.getLongitude(), geoFence.getLatitude(), advertisingModel.getLongitude(), advertisingModel.getLatitude());
                advertisingDto.setDistance(distance);
                responseDto1.getAdvertisingModelList().add(advertisingDto);
            }
            
        }
        return new SuccessResponseDto(response.values());
    }

	public int validateHrefUrl(String href) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(href, HttpMethod.GET, entity, String.class).getStatusCodeValue();
	}

}
