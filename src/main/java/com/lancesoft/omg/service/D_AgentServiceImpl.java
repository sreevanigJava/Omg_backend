package com.lancesoft.omg.service;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.D_AgentDao;
import com.lancesoft.omg.dao.PackingAgentRegistrationDao;
import com.lancesoft.omg.dto.Authorities;
import com.lancesoft.omg.dto.ChangePasswordDto;
import com.lancesoft.omg.dto.D_AgentDto;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.entity.D_AgentEntity;
import com.lancesoft.omg.entity.PackingAgentRegistrationEntity;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOTPException;
import com.lancesoft.omg.exception.UserAlreadyExist;
import com.lancesoft.omg.jwt.JwtUtil;
@Service
public class D_AgentServiceImpl implements D_AgentService {

	@Autowired
	D_AgentDao d_AgentDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	SmsService service;
	@Autowired 
	OtpGenerator otpGenerator;
	@Autowired
	HttpSession httpsession;
	@Autowired
	JwtUtil jwtUtil;
	
	 private static Logger logger = Logger.getLogger(PackingAgentRegistrationServiceImpl.class);
	 
	@Override
	public D_AgentEntity saveUser(D_AgentDto d_AgentDto) {
		logger.info("Admin save user method started...");
		ModelMapper modelMapper = new ModelMapper();
		D_AgentEntity d_Agent = new D_AgentEntity();
		otpGenerator.setOtp();
		if (!validateOTP(d_AgentDto.getPhoneNumber(), d_AgentDto.getUserOtp())) {
			throw new NotValidOTPException("Invalid OTP or Phone number");
		} else {

			if (d_AgentDto == null)

				throw new RuntimeException("null found in registration plss check");

			else
				modelMapper.map(d_AgentDto, d_Agent);

			if (d_AgentDao.existsByuserName(d_Agent.getUserName())
					|| d_AgentDao.existsByphoneNumber(d_Agent.getPhoneNumber())) {
				throw new UserAlreadyExist("UserName is already exists");
			}

			Authorities authorities = new Authorities();
			authorities.setRole("P_Agent");
			List<Authorities> authority = new ArrayList<Authorities>();
			authority.add(authorities);
			d_Agent.setAuthorities(authority);

			if (!(d_Agent.getPassword().equals(d_Agent.getConfirmPassword()))) {
				throw new InvalidEnteredPassword("Password and confirm password must be match");
			}
			d_Agent.setPassword(passwordEncoder.encode(d_Agent.getPassword()));
			d_Agent
					.setConfirmPassword(passwordEncoder.encode(d_Agent.getConfirmPassword()));

			logger.info("Admin save user method end....");
			return d_AgentDao.save(d_Agent);
		}
	}

	@Override
	public boolean validateOTP(String phoneNum, Integer otpNumber) {
		Integer cacheOTP = (Integer) httpsession.getAttribute("otpGntd");
		String phoneNumInSession = (String) httpsession.getAttribute("phoneNumber");
		if (cacheOTP != null && cacheOTP.equals(otpNumber) && phoneNum.equals(phoneNumInSession)) {
			httpsession.invalidate();

			logger.info("Admin validate otp method end..");
			return true;
		}
		logger.info("Admin validate otp method end..");
		return false;
	}
	
	public D_AgentEntity getAgent(String userName) {

		return d_AgentDao.findByuserName(userName);

	}

	public String getMyToken(HttpServletRequest httpServletRequest) {
		logger.info("Start of get my token method..");
		String authorizationHeader = httpServletRequest.getHeader("Authorization");

		String token = null;
		String userName = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			if (!jwtUtil.isTokenExpired(token)) {
				userName = jwtUtil.extractUsername(token);
			} else
				throw new InvalidSession("Invalid Session please login ");
		} else
			throw new InvalidSession("Invalid Session please login ");
		return userName;
	}
	public D_AgentEntity Upadteprofile(D_AgentEntity d_Agent)
	{
		
		return d_AgentDao.save(d_Agent);
	}


public boolean ChangePassword(D_AgentEntity d_Agent ,ChangePasswordDto changepasswordDto)
{
	logger.info("PackingAgent change password method Started...");
	ModelMapper model=new ModelMapper();
	ChangePasswordEntity changepasswordEntity=new ChangePasswordEntity();
	if(!(changepasswordDto==null))
	{
		model.map(changepasswordDto,changepasswordEntity);
	}
		else
		throw new RuntimeException("Null found in changepasswordDto");
	String oldPassword= d_Agent.getPassword();
	
	//String EnterOldPassword=passwordEncoder.encode(changepasswordEntity.getOldPassword());
	boolean isMatch=passwordEncoder.matches(changepasswordEntity.getOldPassword(), oldPassword);
	if(isMatch)
	{
		if(changepasswordEntity.getNewPassword().equals(changepasswordEntity.getConfirmPassword()))
		{
	String enternewpassword=passwordEncoder.encode(changepasswordEntity.getNewPassword());
	//String confirmpassword =passwordEncoder.encode(changepasswordEntity.getConfirmPassword());
	
	
	d_Agent.setPassword(enternewpassword);
	d_Agent.setConfirmPassword(enternewpassword);
	d_AgentDao.save(d_Agent);
	logger.info("packingAgent change password end..");
	return true;
	}
	
	else
	
		throw new InvalidEnteredPassword("NewPassword and ConfirmPassword Must be Match");
	}
	else
		
			throw new InvalidEnteredPassword("old Password Must be Match");
	
}		
}
