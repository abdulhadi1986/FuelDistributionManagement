package com.abulzahab.FuelDistributionManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abulzahab.FuelDistributionManagement.model.Admin;

public interface AdminRepo extends JpaRepository<Admin, String> {
	boolean existsByUserName(String userName);


}
