package com.abulzahab.FuelDistributionManagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abulzahab.FuelDistributionManagement.dao.AdminRepo;
<<<<<<< HEAD
import com.abulzahab.FuelDistributionManagement.dao.StationRepo;
import com.abulzahab.FuelDistributionManagement.dao.VehicleRepo;
import com.abulzahab.FuelDistributionManagement.model.DistributionVehicle;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;

@Component
public class AdminServices {
	
	@Autowired
	private AdminRepo adminRepo;
	
	@Autowired
	private VehicleRepo vehicleRepo;
	
	@Autowired
	private StationRepo stationRepo;
	
	public boolean addOperator(Operator operator) {
		if (!adminRepo.existsById(operator.getNationalNo())) {
			adminRepo.save(operator);
			return true;
		}else {
			return false;
		}
		
	}// end of add operator
	
	
	public boolean addVehicle(DistributionVehicle vehicle) {
		if(!vehicleRepo.existsById(vehicle.getVehicleId())) {
			vehicleRepo.save(vehicle);
			return true;
		}else
			return false;
		
	} // end of add vehicle

	
	public boolean addStation(FuelStation station) {
		
		if(!stationRepo.existsById(station.getStationId())){
			stationRepo.save(station);
			return true;
		}else
			return false;
	}
} // main
	
=======
import com.abulzahab.FuelDistributionManagement.model.Operator;

@Component
public class AdminServices {
	
	@Autowired
	private AdminRepo adminRepo;
	
	public boolean addOperator(Operator operator) {
		if (!adminRepo.existsById(operator.getNationalNo())) {
			adminRepo.save(operator);
			return true;
		}else {
			return false;
		}
		
	}

}
>>>>>>> branch 'Wael' of https://github.com/abdulhadi1986/FuelDistributionManagement.git
