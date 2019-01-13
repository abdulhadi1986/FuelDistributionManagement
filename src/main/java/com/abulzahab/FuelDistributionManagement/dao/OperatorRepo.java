package com.abulzahab.FuelDistributionManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;

public interface OperatorRepo extends JpaRepository<Operator, String> {
	Operator findByFuelStation(FuelStation fuelStation);
	List<Operator> findByFuelStationIsNull();
	boolean existsByUserName(String userName);

}
