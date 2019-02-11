package com.abulzahab.FuelDistributionManagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.Address;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;

public interface StationRepo extends JpaRepository<FuelStation, Integer> {
	List<FuelStation> findByCityAddress(Address address);
	List<FuelStation> findByCityAddressCity (String city);
	List<FuelStation> findByOperator(Optional<Operator> operator);
	FuelStation findByOperator(Operator operator);
	
	
	
}
