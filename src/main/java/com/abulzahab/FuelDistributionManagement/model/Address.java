package com.abulzahab.FuelDistributionManagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Address {
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int addressId;
	private String city;
	private String area;
	
	@OneToMany(mappedBy = "cityAddress" , cascade = CascadeType.ALL , orphanRemoval = true)
	private List<Citizen> citizens;
	
	
	@OneToMany(mappedBy = "cityAddress")
	private List<FuelStation> fuelStations;
	
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}


}
