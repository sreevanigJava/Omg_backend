package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lancesoft.omg.entity.PackingAgentRegistrationEntity;
@Repository
public interface PackingAgentRegistrationDao extends JpaRepository<PackingAgentRegistrationEntity, Integer> {
	PackingAgentRegistrationEntity findByUserName(String useName);
	boolean existsByUserName(String userName);
	boolean existsByPhoneNumber(String userName);
	PackingAgentRegistrationEntity findByPhoneNumber(String phoneNumber);
	
	
}
