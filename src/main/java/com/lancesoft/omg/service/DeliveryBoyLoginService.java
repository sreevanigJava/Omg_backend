package com.lancesoft.omg.service;

import java.text.ParseException;

import com.lancesoft.omg.entity.DeliveryBoyOrders;
import com.lancesoft.omg.entity.Gmail;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.ReturnedOrders;




public interface DeliveryBoyLoginService {
public boolean  sendotp(String phoneNumber) throws ParseException; 
public boolean validateOtp(int userEnteredotp);
public DeliveryBoyOrders assignorders(DeliveryBoyOrders deliveryBoyOrders);

public void sendmail(Gmail gmail,String body,String subject);
public boolean otp(String phoneNumber) throws ParseException;
public boolean validate(int userOtp);
public ReturnedOrders cancledorder(ReturnedOrders returnOrders);


}
