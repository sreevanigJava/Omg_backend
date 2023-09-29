package com.lancesoft.omg.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Zone {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
int id;
@NotNull(message = "Zone name should not be null")
String zoneName;
String country;
String locationName;
}
