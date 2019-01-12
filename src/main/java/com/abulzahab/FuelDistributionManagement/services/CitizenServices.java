package com.abulzahab.FuelDistributionManagement.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.abulzahab.FuelDistributionManagement.dao.RequestRepo;
import com.abulzahab.FuelDistributionManagement.dao.CitizenRepo;
import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;

@Component
public class CitizenServices {
	
	@Autowired
	private CitizenRepo userRepo; 
	
	@Autowired
	private RequestRepo requestRepo;
	
	
	/**
	 * save the new inserted user to the database
	 * @param citizen
	 * @return boolean  true if the user is saved successfully 
	 */
	public Boolean createCitizen(Citizen citizen) {
		//for the citizen the userName and password are national no and id no.
		citizen.setUserName(citizen.getNationalNo());
		citizen.setPassword(citizen.getIdentityNo());
		citizen.setRegisteredRole("citizten");
		userRepo.save(citizen);
		
		return userRepo.existsById(citizen.getNationalNo());
	}
	
	public Boolean submitFuelRequest(FuelRequest fuelRequest) {
		fuelRequest.setSubmitionDate(LocalDateTime.now());
		
		fuelRequest.setStatus("pending");
		//fuelRequest.setSubmittedBy(user);
		
		requestRepo.save(fuelRequest);
		
		return requestRepo.existsById(fuelRequest.getRequestId());
	}

}
