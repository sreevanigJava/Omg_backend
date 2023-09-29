package com.lancesoft.omg.service;

import com.lancesoft.omg.dto.PackingAgentRegistrationDto;
import com.lancesoft.omg.entity.PackingAgentRegistrationEntity;

public interface PackingAgentRegistrationService {
	public PackingAgentRegistrationEntity saveUser(PackingAgentRegistrationDto packingAgentRegistrationDto);
	public boolean validateOTP(String phoneNum,Integer otpNumber);
}
