package com.lancesoft.omg.controller;


import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dao.RegistrationDao;

import com.lancesoft.omg.entity.DeliveryBoyLogin;
import com.lancesoft.omg.entity.DeliveryBoyOrders;
import com.lancesoft.omg.entity.DeliveryBoyOtp;
import com.lancesoft.omg.entity.Gmail;
import com.lancesoft.omg.entity.OrdereduserOtp;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.RegistrationEntity;

import com.lancesoft.omg.entity.ReturnedOrders;
import com.lancesoft.omg.service.DeliveryBoyLoginServiceImpl;

import freemarker.core.ParseException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/delivery")

public class DeliveryBoyLoginController {
	
	@Autowired
	 DeliveryBoyLoginServiceImpl deliveryBoyLoginServiceImpl;
	@Autowired
	RegistrationDao registrationDao;
	
	
@PostMapping("/getotp")
public boolean otp(@RequestBody DeliveryBoyLogin deliveryBoyLogin) throws java.text.ParseException
{
	return deliveryBoyLoginServiceImpl.sendotp(deliveryBoyLogin.getPhoneNumber());
	
}
@PostMapping("/deliveryBoyLogin")
public boolean login(@RequestBody DeliveryBoyOtp deliveryBoyOtp)throws ParseException
{
	//deliveryBoyLoginServiceImpl.setOtp();
	
	return deliveryBoyLoginServiceImpl.validateOtp(deliveryBoyOtp.getUserOtp());

}
@PostMapping("/assignorders")
public DeliveryBoyOrders assign(@RequestBody DeliveryBoyOrders deliveryBoyOrders)
{
	return deliveryBoyLoginServiceImpl.assignorders(deliveryBoyOrders);
}

@PostMapping("/sendgmail")
 
public void send(@RequestBody Gmail gmail,String body,String subject) {

	deliveryBoyLoginServiceImpl.sendmail(gmail, body, subject);
}

@PostMapping("/getotpforusers")
public boolean userOtp(@RequestParam String userName) throws java.text.ParseException
{   RegistrationEntity registrationEntity=registrationDao.findByUserName(userName);

	return deliveryBoyLoginServiceImpl.otp(registrationEntity.getPhoneNumber()) ;
	
}
@PostMapping("/verifyingotp")
public boolean verify(@RequestBody OrdereduserOtp ordereduserOtp )
{
	return deliveryBoyLoginServiceImpl.validate(ordereduserOtp.getOtp());
	
}
@PostMapping("/cancled")
public ReturnedOrders returnorder(@RequestBody ReturnedOrders returnOrders)
{
	return deliveryBoyLoginServiceImpl.cancledorder(returnOrders );
	
}


}