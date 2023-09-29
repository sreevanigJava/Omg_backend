package com.lancesoft.omg.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;



import lombok.Data;

@Data
@Entity
public class Dispatcher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dispatch_id;
	private String orderId;	
	private String orderDate;

	

}
