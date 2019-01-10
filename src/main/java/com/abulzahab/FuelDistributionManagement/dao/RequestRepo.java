package com.abulzahab.FuelDistributionManagement.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;

public interface RequestRepo extends JpaRepository<FuelRequest, Integer> {
	List<FuelRequest> findBySubmittedByAndStatus(Citizen citizen, String status);
	//filter between two dates 
	List<FuelRequest> findBySubmitionDateLessThanAndSubmitionDateGreaterThan(LocalDate dateTo, LocalDate dateFrom); 
	
	//filter between two dates and fuel station 
	List<FuelRequest> findByFuelStationAndSubmitionDateLessThanAndSubmitionDateGreaterThan(FuelStation fuelStation , LocalDate dateTo, LocalDate dateFrom);
	
}
