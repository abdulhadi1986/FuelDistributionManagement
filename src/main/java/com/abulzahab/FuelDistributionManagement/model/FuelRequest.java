package com.abulzahab.FuelDistributionManagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class FuelRequest {

	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int RequestId;
	private double amount;
	
	@ManyToOne
	@JoinColumn(name="FUEL_STATION_ID")
	private FuelStation fuelStation;
	
	private LocalDateTime submitionDate ;
	
	//this is the date format on the web page
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate preferedDeliveryDate;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private Citizen submittedBy;
	
	@ManyToOne
	@JoinColumn(name = "APPROVED_BY")
	private Operator approvedBy;
	
	private String status;
	private LocalDate approvalDate;
	private String comments;
	
	@ManyToOne
	@JoinColumn(name ="assignedDistributionVehicle")
	private DistributionVehicle distributionVehicle;

	public int getRequestId() {
		return RequestId;
	}

	public void setRequestId(int requestId) {
		RequestId = requestId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getSubmitionDate() {
		return submitionDate;
	}

	public void setSubmitionDate(LocalDateTime submitionDate) {
		this.submitionDate = submitionDate;
	}

	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}

	public FuelStation getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}

	public LocalDate getPreferedDeliveryDate() {
		return preferedDeliveryDate;
	}

	public void setPreferedDeliveryDate(LocalDate preferedDeliveryDate) {
		this.preferedDeliveryDate = preferedDeliveryDate.plusDays(1);
	}

	public Citizen getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(Citizen submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Operator getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Operator approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public DistributionVehicle getDistributionVehicle() {
		return distributionVehicle;
	}

	public void setDistributionVehicle(DistributionVehicle distributionVehicle) {
		this.distributionVehicle = distributionVehicle;
	}

	@Override
	public String toString() {
		return "Submitted On: " + submitionDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " \n" +amount+" L"+ ", Station: " + fuelStation +", prefered date: " + preferedDeliveryDate;
	}
	
	
	
	
	
	
}
