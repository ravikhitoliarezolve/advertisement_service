package com.resolve.advertisement.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.advertisement.AppConstantTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resolve.advertisement.entity.AdvertisementEntity;
import com.resolve.advertisement.service.AdvertisementService;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
	@Autowired
    MockMvc mockMvc;
    @InjectMocks
    AdvertisementController advertisementController;
    @Mock
    AdvertisementService advertisementService;
    ObjectMapper mapper = new ObjectMapper();
	
	  @BeforeEach
	    public void setup() {
	        mockMvc = MockMvcBuilders.standaloneSetup(advertisementController)
	                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
	                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView()).build();
	    }

	    @Test
	    void adadvertisement() throws Exception{
	    	AdvertisementEntity advertisementEntity  = new AdvertisementEntity();
	    	advertisementEntity.setAddId(121L);
	    	advertisementEntity.setHref("http://www.facebook.com");
	    	advertisementEntity.setLongitude(2.0);
	    	advertisementEntity.setLatitude(1.0);
	    	advertisementEntity.setAdvertisementName("Test");
	    	advertisementEntity.setUpdatedAt(new Date());
	    	advertisementEntity.setCreatedAt(new Date());
	        String json = mapper.writeValueAsString(advertisementEntity);
	        RequestBuilder mvCRequest = MockMvcRequestBuilders.post(AppConstantTest.CONTROLLER_BASE_URL + "/createAdvertisement")
	                .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON);
	        mockMvc.perform(mvCRequest).andExpect(status().isOk());
	    }

	    @Test
	    void deleteAdvertisement() throws Exception{
	        RequestBuilder mvCRequest = MockMvcRequestBuilders.delete(AppConstantTest.CONTROLLER_BASE_URL + "/deleteAdvertisement/1")
	                .accept(MediaType.APPLICATION_JSON);
	        mockMvc.perform(mvCRequest).andExpect(status().isOk());
	    }

      
	   
	    @Test
	    void getAdvertisement() throws Exception{
	    RequestBuilder mvCRequest = MockMvcRequestBuilders.get(AppConstantTest.CONTROLLER_BASE_URL + "/getAdvertisement?latitude=77.9&longitude=88.9")
	    		.accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(mvCRequest).andExpect(status().isOk());
    }


	    
	    
	    
	    @Test
	    void updateAdvertisement() throws Exception{
	    	AdvertisementEntity advertisementEntity  = new AdvertisementEntity();
	    	advertisementEntity.setAddId(121L);
	    	advertisementEntity.setHref("https://www.facebook.com");
	    	advertisementEntity.setLongitude(22.01);
	    	advertisementEntity.setLatitude(31.20);
	    	advertisementEntity.setAdvertisementName("Test");
	    	advertisementEntity.setUpdatedAt(new Date());
	    	advertisementEntity.setCreatedAt(new Date());
	        String json = mapper.writeValueAsString(advertisementEntity);
	        RequestBuilder mvCRequest = MockMvcRequestBuilders.put(AppConstantTest.CONTROLLER_BASE_URL + "/updateAdvertisement")
	                .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON);
	        mockMvc.perform(mvCRequest).andExpect(status().isOk());
	    }

	}



