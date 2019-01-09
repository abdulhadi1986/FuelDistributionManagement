package com.abulzahab.FuelDistributionManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;

public interface RequestRepo extends JpaRepository<FuelRequest, Integer> {
	List<FuelRequest> findBySubmittedByAndStatus(Citizen citizen, String status);

}
