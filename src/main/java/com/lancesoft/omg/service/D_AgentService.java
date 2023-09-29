package com.lancesoft.omg.service;

import com.lancesoft.omg.dto.D_AgentDto;
import com.lancesoft.omg.entity.D_AgentEntity;

public interface D_AgentService {
	public D_AgentEntity saveUser(D_AgentDto d_AgentDto);
	public boolean validateOTP(String phoneNum,Integer otpNumber);
}
