 package com.lancesoft.omg.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.ChangePasswordDto;
import com.lancesoft.omg.dto.D_AgentDto;

import com.lancesoft.omg.entity.D_AgentEntity;

import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.service.D_AgentServiceImpl;

import com.lancesoft.omg.service.SmsService;
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class D_AgentController {
private D_AgentServiceImpl d_AgentServiceImpl;
	
	public D_AgentController(D_AgentServiceImpl d_AgentServiceImpl) {
		super();
		this.d_AgentServiceImpl = d_AgentServiceImpl;
	}
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private SmsService service;
	private final String TOPIC_DESTINATION = "/lesson/sms";

	@PostMapping("/D_Agent/sendOtp")
	public ResponseEntity<Boolean> smsSubmit(@RequestBody SmsEntity sms) {
		try {

			service.send(sms);

		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent !: " + sms.getPhoneNumber());

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	private String getTimeStamp() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}
	
	@PostMapping("/D_Agent/register")
	public ResponseEntity<D_AgentEntity> createUser(@RequestBody @Valid D_AgentDto d_AgentDto )
	{
		return new ResponseEntity(d_AgentServiceImpl.saveUser(d_AgentDto),HttpStatus.OK);
	}

	@GetMapping("/D_Agent/myProfile")
	public ResponseEntity<D_AgentEntity> getMyProfile(HttpServletRequest httpServletRequest) {
		String userName = d_AgentServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity(d_AgentServiceImpl.getAgent(userName), HttpStatus.OK);
	}
	@PutMapping("/D_Agent/UPdate")
	public ResponseEntity<D_AgentEntity>update(@RequestBody @Valid D_AgentEntity d_AgentEntity)
	{
	return new ResponseEntity(d_AgentServiceImpl.Upadteprofile(d_AgentEntity),HttpStatus.OK);
}
	@PutMapping("/D_Agent/password")
	public String changePassword(@RequestBody ChangePasswordDto changepasswordDto,Authentication authentication,HttpServletRequest httpServletRequest)
	{
		String username=d_AgentServiceImpl.getMyToken(httpServletRequest);
		D_AgentEntity d_AgentEntity=d_AgentServiceImpl.getAgent(username);
		boolean passwordChanged=d_AgentServiceImpl.ChangePassword(d_AgentEntity, changepasswordDto);
		if(passwordChanged)
		{
			return "Password has been Changed";
		}
		return "Error in changing password";

}
}
