package com.resolve.advertisement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import lombok.Data;
@Data
@Entity
@Table(name="advertisement_master")
public class AdvertisementEntity extends AuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addId;
	 @NotNull
	    @Digits(integer = 6, fraction = 7, message = "at max 7 precision allowed")
	private double latitude;
	 @NotNull
	    @Digits(integer = 6, fraction = 7, message = "at max 7 precision allowed")
	private double longitude;
	 @NotBlank
	    @URL(message = "href is invalid")
	private String href;
	 @NotBlank
	private String advertisementName;

}
