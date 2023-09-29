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


import com.lancesoft.omg.dao.PackingAgentRegistrationDao;
import com.lancesoft.omg.dto.Authorities;
import com.lancesoft.omg.dto.ChangePasswordDto;
import com.lancesoft.omg.dto.PackingAgentRegistrationDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.entity.PackingAgentRegistrationEntity;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOTPException;
import com.lancesoft.omg.exception.UserAlreadyExist;
import com.lancesoft.omg.jwt.JwtUtil;

@Service
public class PackingAgentRegistrationServiceImpl implements PackingAgentRegistrationService{
@Autowired
PackingAgentRegistrationDao packingAgentRegistrationDao;
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
	public PackingAgentRegistrationEntity saveUser(PackingAgentRegistrationDto packingAgentRegistrationDto) {
		
		logger.info("Admin save user method started...");
		ModelMapper modelMapper = new ModelMapper();//d
		PackingAgentRegistrationEntity packing = new PackingAgentRegistrationEntity();//d
		otpGenerator.setOtp();
		if (!validateOTP(packingAgentRegistrationDto.getPhoneNumber(), packingAgentRegistrationDto.getUserOtp())) {
			throw new NotValidOTPException("Invalid OTP or Phone number");
		} else {

			if (packingAgentRegistrationDto == null)

				throw new RuntimeException("null found in registration plss check");

			else
				modelMapper.map(packingAgentRegistrationDto, packing);

			if (packingAgentRegistrationDao.existsByUserName(packing.getUserName())
					|| packingAgentRegistrationDao.existsByPhoneNumber(packing.getPhoneNumber())) {
				throw new UserAlreadyExist("UserName is already exists");
			}

			Authorities authorities = new Authorities();
			authorities.setRole("P_Agent");
			List<Authorities> authority = new ArrayList<Authorities>();
			authority.add(authorities);
			packing.setAuthorities(authority);

			if (!(packing.getPassword().equals(packing.getConfirmPassword()))) {
				throw new InvalidEnteredPassword("Password and confirm password must be match");
			}
			packing.setPassword(passwordEncoder.encode(packing.getPassword()));
			packing
					.setConfirmPassword(passwordEncoder.encode(packing.getConfirmPassword()));

			logger.info("Admin save user method end....");
			return packingAgentRegistrationDao.save(packing);
		}
	}
	
	public boolean validateOTP(String phoneNum, Integer otpNumber) {
		// get OTP from cache
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
	
	
	public PackingAgentRegistrationEntity getAgent(String userName) {

		return packingAgentRegistrationDao.findByUserName(userName);

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
	
	
	public PackingAgentRegistrationEntity Upadteprofile(PackingAgentRegistrationEntity packingAgentRegistrationEntity)
	{
		
		return packingAgentRegistrationDao.save(packingAgentRegistrationEntity);
	}
	
	
	
	public boolean ChangePassword(PackingAgentRegistrationEntity packingAgentRegistrationEntity ,ChangePasswordDto changepasswordDto)
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
		String oldPassword= packingAgentRegistrationEntity.getPassword();
		
		//String EnterOldPassword=passwordEncoder.encode(changepasswordEntity.getOldPassword());
		boolean isMatch=passwordEncoder.matches(changepasswordEntity.getOldPassword(), oldPassword);
		if(isMatch)
		{
			if(changepasswordEntity.getNewPassword().equals(changepasswordEntity.getConfirmPassword()))
			{
		String enternewpassword=passwordEncoder.encode(changepasswordEntity.getNewPassword());
		//String confirmpassword =passwordEncoder.encode(changepasswordEntity.getConfirmPassword());
		
		
		packingAgentRegistrationEntity.setPassword(enternewpassword);
		packingAgentRegistrationEntity.setConfirmPassword(enternewpassword);
		packingAgentRegistrationDao.save(packingAgentRegistrationEntity);
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

