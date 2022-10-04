package com.resolve.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resolve.advertisement.entity.AdvertismentGeoMapping;

@Repository
public interface AdvertisementGeoMappingRepository extends JpaRepository<AdvertismentGeoMapping, Long> {

}
