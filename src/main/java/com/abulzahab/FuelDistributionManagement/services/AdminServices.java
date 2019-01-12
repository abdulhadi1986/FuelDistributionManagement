package com.abulzahab.FuelDistributionManagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abulzahab.FuelDistributionManagement.dao.AdminRepo;
import com.abulzahab.FuelDistributionManagement.dao.VehicleRepo;
import com.abulzahab.FuelDistributionManagement.model.DistributionVehicle;
import com.abulzahab.FuelDistributionManagement.model.Operator;

@Component
public class AdminServices {
	
	@Autowired
	private AdminRepo adminRepo;
	
	@Autowired
	private VehicleRepo vehicleRepo;
	
	public boolean addOperator(Operator operator) {
		if (!adminRepo.existsById(operator.getNationalNo())) {
			operator.setRegisteredRole("operator");
			adminRepo.save(operator);
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean addVehicle(DistributionVehicle vehicle) {
		if(!vehicleRepo.existsById(vehicle.getVehicleId())) {
			vehicleRepo.save(vehicle);
			return true;
		}else
			return false;
	}



}
