package com.abulzahab.FuelDistributionManagement.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class DistributionVehicle {
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int vehicleId;
	private String plateNo;
	private double tankCapacity;
	private String driverName;
	private String driverMobileNo;
	
	@ManyToOne
	@JoinColumn(name = "FUEL_STATION_ID")
	private FuelStation fuelStation;
	
	@OneToMany(mappedBy="distributionVehicle")
	private List<FuelRequest> assignedFuelRequests;
	
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public double getTankCapacity() {
		return tankCapacity;
	}
	public void setTankCapacity(double tankCapacity) {
		this.tankCapacity = tankCapacity;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverMobileNo() {
		return driverMobileNo;
	}
	public void setDriverMobileNo(String driverMobileNo) {
		this.driverMobileNo = driverMobileNo;
	}
	public FuelStation getFuelStation() {
		return fuelStation;
	}
	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}
	@Override
	public String toString() {
		return "ID: " + vehicleId + ", plateNo=" + plateNo + ", tank=" + tankCapacity
				+ "L , " + fuelStation+ ", contact: "+ driverMobileNo;
	}
	
	
}