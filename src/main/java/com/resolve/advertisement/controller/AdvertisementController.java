package com.resolve.advertisement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resolve.advertisement.dto.ResponseDto;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.service.AdvertisementService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/advertisement/api")
public class AdvertisementController {
	
	@Autowired
	AdvertisementService advertisementService;

	@PostMapping("/createAdvertisement")
	public ResponseDto addAdvertisement(@Valid @RequestBody AdvertisementEntity request) {
		log.info("creating advertisement for request {}", request);
		return advertisementService.addAdvertisement(request);
	}
	
	@DeleteMapping("deleteAdvertisement" + "/{advertisingId}")
	public ResponseDto deleteAdvertisement(@PathVariable final Long advertisingId) {
		log.info("deleting advertising for request {}", advertisingId);
		return advertisementService.deleteAdvertisement(advertisingId);
	}
	
	   @GetMapping("/getAdvertisement")
	    public ResponseDto getAdvertising(@RequestParam final Double latitude,@RequestParam final Double longitude){
	        log.info("getting advertising for request lat {} long {}",latitude,longitude);
	        return advertisementService.getAdvertisementInsideGeo(latitude,longitude);
	    }

	@PutMapping("updateAdvertisement")
	public ResponseDto updateAdvertisement(@Valid @RequestBody AdvertisementEntity request) {
		log.info("updating advertising for request {}", request);
		return advertisementService.updateAdvertisement(request);
	}

}



