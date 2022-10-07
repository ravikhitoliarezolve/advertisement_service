package com.resolve.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resolve.advertisement.entity.AdvertisementGeoMapping;

@Repository
public interface AdvertisementGeoMappingRepository extends JpaRepository<AdvertisementGeoMapping, Long> {

}
