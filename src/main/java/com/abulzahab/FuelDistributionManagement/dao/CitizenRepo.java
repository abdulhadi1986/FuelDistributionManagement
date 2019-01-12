package com.abulzahab.FuelDistributionManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.Citizen;

public interface CitizenRepo extends JpaRepository<Citizen, String> {
	
	
}
