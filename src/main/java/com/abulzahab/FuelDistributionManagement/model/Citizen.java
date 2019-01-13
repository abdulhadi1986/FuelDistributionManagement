package com.abulzahab.FuelDistributionManagement.model;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;


@Entity
@DiscriminatorValue(value="CITIZEN")
public class Citizen extends User {
	
	private String identityNo;
	
	@ManyToOne
	@JoinColumn(name="CITY_ADDRESS")
	@NotNull (message = "the area must be filled")
	private Address cityAddress;
	
	private String street;
	private String building; 
	
	
	@OneToMany(mappedBy ="submittedBy")
	private List<FuelRequest> submittedFuelRequests;
	
	
	public String getIdentityNo() {
		return identityNo;
	}
	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}
	
	public Address getCityAddress() {
		return cityAddress;
	}
	public void setCityAddress(Address cityAddress) {
		this.cityAddress = cityAddress;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public List<FuelRequest> getSubmittedFuelRequests() {
		return submittedFuelRequests;
	}
	public void setSubmittedFuelRequests(List<FuelRequest> submittedFuelRequests) {
		this.submittedFuelRequests = submittedFuelRequests;
	}
	
	
	@Override
	public String toString() {
		return ""+ getFirstName() + " " + getLastName() + "  " +getCityAddress();
	}
	

}
