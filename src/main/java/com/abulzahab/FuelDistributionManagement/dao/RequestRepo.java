package com.abulzahab.FuelDistributionManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;
<<<<<<< HEAD
import com.abulzahab.FuelDistributionManagement.model.Operator;

public interface RequestRepo extends JpaRepository<FuelRequest, Integer> {
	List<FuelRequest> findBySubmittedByAndStatus(Citizen citizen, String status);
	List<FuelRequest> findByFuelStationOperatorAndStatus(Operator operator, String status);

=======

public interface RequestRepo extends JpaRepository<FuelRequest, Integer> {
	List<FuelRequest> findBySubmittedByAndStatus(Citizen citizen, String status);
>>>>>>> branch 'Wael' of https://github.com/abdulhadi1986/FuelDistributionManagement.git

}
