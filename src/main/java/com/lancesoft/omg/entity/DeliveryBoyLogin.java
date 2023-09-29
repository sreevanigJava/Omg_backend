package com.lancesoft.omg.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="D_BoyStatus")
public class DeliveryBoyLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int D_BoyId;
 private String phoneNumber;
}
