package com.abulzahab.FuelDistributionManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.DistributionVehicle;

public interface VehicleRepo extends JpaRepository<DistributionVehicle, Integer> {

}
