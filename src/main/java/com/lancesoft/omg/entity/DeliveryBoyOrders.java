package com.lancesoft.omg.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy.Strategy;

@Data
@Entity
@Table(name="deliveryBoyOrders")
public class DeliveryBoyOrders {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int deliveryid;
	private String orderid;
	private String phoneNo;
	private String deliveryDate;
	
}
