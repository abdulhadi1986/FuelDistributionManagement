package com.abulzahab.FuelDistributionManagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abulzahab.FuelDistributionManagement.model.Address;

public interface AddressRepo extends JpaRepository<Address, Integer> {

}
