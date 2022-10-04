package com.resolve.advertisement.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "advertisment_geo_mapping")
@NoArgsConstructor
public class AdvertismentGeoMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long geoId;
	private Long advId;

	public AdvertismentGeoMapping(Long geoId, Long advId) {
		super();
		this.geoId = geoId;
		this.advId = advId;
	}

}
