package com.abulzahab.FuelDistributionManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.User;

public interface UserRepo extends JpaRepository<User, String> {
	
	User findByUserNameAndPassword(String userName, String password);
	boolean existsByUserNameAndPassword(String userName, String password);

}
