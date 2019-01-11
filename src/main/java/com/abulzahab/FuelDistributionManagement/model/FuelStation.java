package com.abulzahab.FuelDistributionManagement.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class FuelStation {
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int stationId;
	private String stationName;
	private String telNo;
	
	@ManyToOne
	@JoinColumn(name="cityAddress")
	private Address cityAddress;
	private double tankCapacity;
	
	@OneToOne
	private Operator operator;
	
	@OneToMany(mappedBy = "fuelStation")
	private List<DistributionVehicle> distributionVehicles;
	
	@OneToMany(mappedBy = "fuelStation")
	private List<FuelRequest> assignedRequests;
	
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public Address getCityAddress() {
		return cityAddress;
	}
	public void setCityAddress(Address cityAddress) {
		this.cityAddress = cityAddress;
	}
	public double getTankCapacity() {
		return tankCapacity;
	}
	public void setTankCapacity(double tankCapacity) {
		this.tankCapacity = tankCapacity;
	}
	public List<DistributionVehicle> getDistributionVehicles() {
		return distributionVehicles;
	}
	public void setDistributionVehicles(List<DistributionVehicle> distributionVehicles) {
		this.distributionVehicles = distributionVehicles;
	}
	@Override
	public String toString() {
		return stationName;
	}



}