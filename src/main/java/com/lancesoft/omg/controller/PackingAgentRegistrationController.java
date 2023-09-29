package com.lancesoft.omg.controller;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.ChangePasswordDto;
import com.lancesoft.omg.dto.PackingAgentRegistrationDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.Dispatcher;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.PackingAgentRegistrationEntity;

import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.service.CategoriesService;
import com.lancesoft.omg.service.PackingAgentRegistrationServiceImpl;
import com.lancesoft.omg.service.ProductsService;
import com.lancesoft.omg.service.SmsService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PackingAgentRegistrationController {
	private PackingAgentRegistrationServiceImpl packingAgentRegistrationServiceImpl;
	
	public PackingAgentRegistrationController(PackingAgentRegistrationServiceImpl packingAgentRegistrationServiceImpl) {
		super();
		this.packingAgentRegistrationServiceImpl = packingAgentRegistrationServiceImpl;
	}
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private SmsService service;
	@Autowired
	CategoriesService adminDashBoardServiceImpl;
	@Autowired
	ProductsService productsServiceImpl;
	private final String TOPIC_DESTINATION = "/lesson/sms";

	@PostMapping("/P_Agent/sendOtp")
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
	
	@PostMapping("/P_Agent/register")
	public ResponseEntity<PackingAgentRegistrationEntity> createUser(@RequestBody @Valid PackingAgentRegistrationDto packingAgentRegistrationDto )
	{
		return new ResponseEntity(packingAgentRegistrationServiceImpl.saveUser(packingAgentRegistrationDto),HttpStatus.OK);
	}

	@GetMapping("/P_Agent/myProfile")
	public ResponseEntity<PackingAgentRegistrationEntity> getMyProfile(HttpServletRequest httpServletRequest) {
		String userName = packingAgentRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity(packingAgentRegistrationServiceImpl.getAgent(userName), HttpStatus.OK);
	}
	@PutMapping("/P_Agent/UPdate")
	public ResponseEntity<PackingAgentRegistrationEntity>update(@RequestBody @Valid PackingAgentRegistrationEntity packingAgentRegistrationEntity)
	{
	return new ResponseEntity(packingAgentRegistrationServiceImpl.Upadteprofile(packingAgentRegistrationEntity),HttpStatus.OK);
}
	@PutMapping("/P_Agent/password")
	public String changePassword(@RequestBody ChangePasswordDto changepasswordDto,Authentication authentication,HttpServletRequest httpServletRequest)
	{
		String username=packingAgentRegistrationServiceImpl.getMyToken(httpServletRequest);
		PackingAgentRegistrationEntity packingAgentRegistrationEntity=packingAgentRegistrationServiceImpl.getAgent(username);
		boolean passwordChanged=packingAgentRegistrationServiceImpl.ChangePassword(packingAgentRegistrationEntity, changepasswordDto);
		if(passwordChanged)
		{
			return "Password has been Changed";
		}
		return "Error in changing password";

}
	@PostMapping("/dipatch")
	public Dispatcher dispatch(@RequestBody Dispatcher dispatcher) {

		return productsServiceImpl.dispatched(dispatcher);
	}

	@GetMapping("/dispatchedByDate")
	public List<Dispatcher> dispatchDate(@RequestParam String orderDate) {
		return productsServiceImpl.dispatcher(orderDate);
	}

	@GetMapping("/getorderid")
	public OrdersEntity getorderid(@RequestParam String orderId) {
		return productsServiceImpl.getorderid(orderId);
	}

}

