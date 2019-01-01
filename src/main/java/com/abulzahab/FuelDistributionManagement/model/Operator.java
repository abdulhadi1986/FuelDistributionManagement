package com.abulzahab.FuelDistributionManagement.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue (value ="OPERATOR")
public class Operator extends User{
	
	@OneToOne
	private FuelStation fuelStation; 
	
	@OneToMany(mappedBy="approvedBy")
	private List<FuelRequest> approvedFuelRequests;

	public FuelStation getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}

	public List<FuelRequest> getApprovedFuelRequests() {
		return approvedFuelRequests;
	}

	public void setApprovedFuelRequests(List<FuelRequest> approvedFuelRequests) {
		this.approvedFuelRequests = approvedFuelRequests;
	}
	
	
	
	
		  
	 

}
