package com.lancesoft.omg.entity;

import javax.annotation.processing.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Locations {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
  String country;
  @NotNull
  String locationName;
  
	


}
