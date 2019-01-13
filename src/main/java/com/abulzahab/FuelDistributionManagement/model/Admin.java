package com.abulzahab.FuelDistributionManagement.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue (value="ADMIN")
public class Admin extends User{

	@Override
	public String toString() {
		return "Admin: " + getFirstName() + " " + getLastName() + "";
	}
	
}
