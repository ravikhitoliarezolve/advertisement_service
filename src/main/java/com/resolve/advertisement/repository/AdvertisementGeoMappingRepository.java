package com.resolve.advertisement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resolve.advertisement.entity.AdvertisementGeoMapping;

@Repository
public interface AdvertisementGeoMappingRepository extends JpaRepository<AdvertisementGeoMapping, Long> {

	@Query(value = "SELECT * FROM advertisment_geo_mapping m WHERE m.geo_id IN :geoId",nativeQuery = true)
    List<AdvertisementGeoMapping> findByGeoIds(@Param("geoId") List<Long> geoId);
}
