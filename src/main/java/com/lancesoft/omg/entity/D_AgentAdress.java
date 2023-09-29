package com.lancesoft.omg.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.lancesoft.omg.idGenerator.CustomeIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class D_AgentAdress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id ;
    private String userName;
    private String houseNumber ;
    private String streetName;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private long pincode;
    private boolean currentAddress;
}
