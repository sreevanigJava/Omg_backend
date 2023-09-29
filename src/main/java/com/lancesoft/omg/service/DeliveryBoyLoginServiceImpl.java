package com.lancesoft.omg.service;
import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.D_AgentDao;
import com.lancesoft.omg.dao.DeliveryBoyLoginDao;
import com.lancesoft.omg.dao.DeliveryBoyOrdersDao;
import com.lancesoft.omg.dao.OrdersDao;
import com.lancesoft.omg.dao.RegistrationDao;
import com.lancesoft.omg.dao.ReturnedOrdersDao;


import com.lancesoft.omg.entity.DeliveryBoyOrders;
import com.lancesoft.omg.entity.Gmail;
import com.lancesoft.omg.entity.OrdersEntity;


import com.lancesoft.omg.entity.ReturnedOrders;
import com.lancesoft.omg.exception.CustomException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;

import com.twilio.type.PhoneNumber;



import org.apache.log4j.Logger;

@Service
public class DeliveryBoyLoginServiceImpl implements DeliveryBoyLoginService {
	@Autowired
	SmsService smsService;
	@Autowired
	OtpService otpService;
	@Autowired
	OtpGenerator otpGenerator;
	@Autowired
	D_AgentDao d_AgentDao;
	@Autowired
	DeliveryBoyOrdersDao deliveryBoyOrdersDao;
	@Autowired
	HttpSession httpSession;
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	RegistrationDao registrationDao;
	@Autowired
	ReturnedOrdersDao returnedOrdersDao;
	@Autowired
	OrdersDao ordersDao;
	@Autowired
	DeliveryBoyLoginDao deliveryBoyLoginDao;

	 
	private final String ACCOUNT_SID = "AC562c011ab3a76d32b48b25da94506129";
	private final String AUTH_TOKEN = "de246d600e2eb3a44a381a8935c8bf63";
	private final String FROM_NUMBER = "+14344438017";

	 private static Logger logger = Logger.getLogger(DeliveryBoyLoginServiceImpl.class);
	@Override
	public boolean sendotp(String phoneNumber) throws ParseException {
		
		//validate user by his phone number
		if(d_AgentDao.existsByphoneNumber(phoneNumber))
		{
			//generating Otp
			int min = 100000;
			int max = 999999;
			int genrtedOtp = (int) (Math.random() * (max - min + 1) + min);
			
			//setting in session
			httpSession.setAttribute("abc", genrtedOtp);
			
			//sending to user
			this.send(phoneNumber,genrtedOtp);
		
			return true;
		}
		else 
			throw new CustomException("Invalid");
	}
	public void send(String phoneNumber ,int genrtedOtp) throws ParseException {

	
		logger.info("Start of send method..");
		try {
			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		
			String msg = "Your OTP -" + genrtedOtp + " Please verify this otp in your application for login";

			MessageCreator creater = Message.creator(new PhoneNumber(phoneNumber),
					new PhoneNumber(FROM_NUMBER), msg);
			creater.create();
		} catch (Exception ex) {
			
			throw new CustomException("Unable to send Otp ");
		}
		
		logger.info("End of send method..");
	}
	public boolean validateOtp(int userEnteredotp) {
	int sessionOtp=	(int) httpSession.getAttribute("abc");
		
		if(userEnteredotp!=0 && userEnteredotp==sessionOtp)
		{
			httpSession.invalidate();
			return true;
		
		}
		
		return false;
	}
	@Override
	public DeliveryBoyOrders assignorders(DeliveryBoyOrders deliveryBoyOrders) {
		
		return deliveryBoyOrdersDao.save(deliveryBoyOrders);
	}
	@Override
	public void sendmail(Gmail gmail,String body,String subject ) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("onlinesupermarket37@gmail.com");
		message.setTo(gmail.getToEmail());
		String body1="hi  "+gmail.getUserName()+" you got an order for delivery and it's order id is "+gmail.getOrderid()+" once check  your orders";
		message.setText(body1);
		String subject1="order Alert....";
		message.setSubject(subject1);
		javaMailSender.send(message);
	}
	@Override
	public boolean otp(String phoneNumber) throws ParseException {
		if(d_AgentDao.existsByphoneNumber(phoneNumber))
		{
			int min=100000;
			int max=999999;
			int generatedotp=(int) (Math.random() * (max - min + 1) + min);
			httpSession.setAttribute("number", generatedotp);
			this.send(phoneNumber, generatedotp);
			return true;
		}
		else 
			throw new CustomException("invalid numberx");
	}
		
	public void sendingOtp(String phoneNumber,String generatedotp)
	{
		try {
	
		logger.info("Start of send method..");
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		String message="your otp"+generatedotp+"please verify it with  your delivery agent";
		MessageCreator creater=Message.creator(new PhoneNumber(phoneNumber),new PhoneNumber(FROM_NUMBER),message);
		creater.create();
	}
		catch(Exception ex)
		{
			throw new CustomException("Unable to send Otp");
		}
		logger.info("End of send method..");
	}
	@Override
	public boolean validate(int userOtp) {
		int session=(int) httpSession.getAttribute("number");
		if(session!=0&& userOtp==session)
		{
			httpSession.invalidate();
			return true;
		}
		return false;
	}
	@Override
	public ReturnedOrders cancledorder(ReturnedOrders returnOrders) {
		
		 ReturnedOrders order= returnedOrdersDao.save(returnOrders);
		 
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("onlinesupermarket37@gmail.com");
		message.setTo("2swathi000@gmail.com");
		String body1="hi this orderid  "+returnOrders.getOrderid()+"  returned due to "+ returnOrders.getReason();
		message.setText(body1);
		String subject1="Returned Orders";
		message.setSubject(subject1);
		javaMailSender.send(message);
		return  order;
	}






	
}
