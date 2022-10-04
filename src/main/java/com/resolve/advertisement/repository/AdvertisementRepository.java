package com.resolve.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resolve.advertisement.entity.AdvertisementEntity;

public interface AdvertisementRepository extends JpaRepository<AdvertisementEntity,Long> {
}
