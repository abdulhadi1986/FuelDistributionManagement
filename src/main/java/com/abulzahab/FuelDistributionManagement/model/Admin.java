package com.abulzahab.FuelDistributionManagement.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue (value="ADMIN")
public class Admin extends User{
	//public static final String ROLE = "ADMIN";
	
}
