package com.abulzahab.FuelDistributionManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.User;

public interface UsrRepo extends JpaRepository<User, String> {
	String findByUserName(String userName);
	boolean findByUserNameAndPassword(String userName, String Password);

}
