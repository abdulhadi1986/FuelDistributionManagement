package com.abulzahab.FuelDistributionManagement.services;

<<<<<<< HEAD
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abulzahab.FuelDistributionManagement.dao.RequestRepo;
import com.abulzahab.FuelDistributionManagement.dao.UserRepo;
import com.abulzahab.FuelDistributionManagement.dao.UsrRepo;
import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;
import com.abulzahab.FuelDistributionManagement.model.User;

@Component
public class CitizenServices {
	
	@Autowired
	private UserRepo userRepo; 
	
	@Autowired
	private RequestRepo requestRepo;
	
	@Autowired
	private UsrRepo usrRepo;
	
	
	/**
	 * save the new inserted user to the database
	 * @param citizen
	 * @return boolean  true if the user is saved successfully 
	 */
	public Boolean createCitizen(Citizen citizen) {
		//for the citizen the userName and password are national no and id no.
		citizen.setUserName(citizen.getNationalNo());
		citizen.setPassword(citizen.getIdentityNo()); 
		userRepo.save(citizen);
		
		return userRepo.existsById(citizen.getNationalNo());
	}
	
	public Boolean submitFuelRequest(FuelRequest fuelRequest) {
		fuelRequest.setSubmitionDate(LocalDate.now());
		fuelRequest.setApproved(false);
		fuelRequest.setStatus("pending");
		//fuelRequest.setSubmittedBy(user);
		
		requestRepo.save(fuelRequest);
		
		return requestRepo.existsById(fuelRequest.getRequestId());
	}
	
	public boolean IsUser(User user) {
		if(usrRepo.findByUserNameAndPassword(user.getUserName(), user.getPassword())) {	
				return true;	
		}else 
			return false;
			
			
=======
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.abulzahab.FuelDistributionManagement.dao.RequestRepo;
import com.abulzahab.FuelDistributionManagement.dao.UserRepo;
import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;

@Component
public class CitizenServices {
	
	@Autowired
	private UserRepo userRepo; 
	
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
		userRepo.save(citizen);
		
		return userRepo.existsById(citizen.getNationalNo());
	}
	
	public Boolean submitFuelRequest(FuelRequest fuelRequest) {
		fuelRequest.setSubmitionDate(LocalDateTime.now());
		
		fuelRequest.setStatus("pending");
		//fuelRequest.setSubmittedBy(user);
		
		requestRepo.save(fuelRequest);
		
		return requestRepo.existsById(fuelRequest.getRequestId());
>>>>>>> branch 'Wael' of https://github.com/abdulhadi1986/FuelDistributionManagement.git
	}

}
