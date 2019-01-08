package com.abulzahab.FuelDistributionManagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abulzahab.FuelDistributionManagement.dao.StationRepo;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;

@Component
public class OperationServices {
	
	
	@Autowired
	private StationRepo stationRepo;
	
	public String getStationStatus(Operator operator) {
	
	FuelStation station = stationRepo.findByOperator(operator);
	
	double status = station.getTankCapacity();
	
	if(status > 100)
		return "The Tank Is Full";
	else
		return "The Tank Is Under 100 Leters";
	}
	
	public void addFuel(Operator operator, double amount) {
		
		FuelStation station = new FuelStation();
		station=stationRepo.findByOperator(operator);
		double newAmount = station.getTankCapacity();
		newAmount += amount;
		station.setTankCapacity(newAmount);
		stationRepo.save(station);
		
	}


}// main
