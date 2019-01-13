package com.abulzahab.FuelDistributionManagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.DistributionVehicle;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;

public interface VehicleRepo extends JpaRepository<DistributionVehicle, Integer> {
	List<DistributionVehicle> findByFuelStation(FuelStation fuelStation);
	List<DistributionVehicle> findByFuelStationIsNull();
	List<DistributionVehicle> findByFuelStationNotNull();

}
