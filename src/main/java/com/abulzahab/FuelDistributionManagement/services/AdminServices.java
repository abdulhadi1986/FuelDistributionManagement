package com.abulzahab.FuelDistributionManagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abulzahab.FuelDistributionManagement.dao.AdminRepo;
import com.abulzahab.FuelDistributionManagement.dao.OperatorRepo;
import com.abulzahab.FuelDistributionManagement.dao.VehicleRepo;
import com.abulzahab.FuelDistributionManagement.model.Admin;
import com.abulzahab.FuelDistributionManagement.model.DistributionVehicle;
import com.abulzahab.FuelDistributionManagement.model.Operator;

@Component
public class AdminServices {
	
	@Autowired
	private OperatorRepo operatorRepo;
	
	@Autowired
	private AdminRepo adminRepo;
	
	@Autowired
	private VehicleRepo vehicleRepo;
	
	public boolean addOperator(Operator operator) {
		if (!operatorRepo.existsById(operator.getNationalNo())) {
			if(!operatorRepo.existsByUserName(operator.getUserName())) {
			operator.setRegisteredRole("operator");
			operatorRepo.save(operator);
			}
		}else {
			operatorRepo.save(operator);
			
		}
		return true;
	}
	
	
	///// Added By WAEL
	public boolean addAdmin(Admin admin) {
		if (!adminRepo.existsById(admin.getNationalNo())) {
			if(!adminRepo.existsByUserName(admin.getUserName())) {
				admin.setRegisteredRole("admin");
				adminRepo.save(admin);
			}

			
		}else {
			adminRepo.save(admin);
			
		}
		return true;
	}
	
	public boolean addVehicle(DistributionVehicle vehicle) {
		if(!vehicleRepo.existsById(vehicle.getVehicleId())) {
			vehicleRepo.save(vehicle);
			return true;
		}else
			return false;
	}



}
