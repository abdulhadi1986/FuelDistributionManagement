package com.abulzahab.FuelDistributionManagement.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "USERS")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "ROLE")
public abstract class User {


	@Id
	@Column(name = "USER_ID")
	@NotNull
	@Size(min=5, max=5, message = "national number must be 5 numbers")
	private String nationalNo;
	private String firstName;
	private String lastName;
	@Email(regexp = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9._%-]+\\.[a-zA-Z]{2,4}")
	private String email;
	private String mobileNo;
	private String userName;
	private String password;
	public  String registeredRole;
			
	public String getNationalNo() {
		return nationalNo;
	}
	public void setNationalNo(String nationalNo) {
		this.nationalNo = nationalNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPassword() {
		return this.password;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPassword(String password) {
		this.password = password; 
	}
	
	public String getRegisteredRole() {
		return registeredRole;
	}
	public void setRegisteredRole(String registeredRole) {
		this.registeredRole = registeredRole;
	}
	public String getRole() {
		return this.getRole();
	}
	
	@Override
	public String toString() {
		return "User National No=" + nationalNo + ", First Name=" + firstName + ", Last Name=" + lastName + ", Email="
				+ email + ", mobileNo=" + mobileNo + ", userName=" + userName + ", password=" + password
				+ ", Registered Role=" + registeredRole + "";
	}
	
	

}
