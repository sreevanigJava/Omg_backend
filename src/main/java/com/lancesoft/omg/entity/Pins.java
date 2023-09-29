package com.lancesoft.omg.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data

public class Pins {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
 private String country;
 @NotBlank(message=" Location Name should not be blank")
 private String locationName;
 @NotBlank(message=" zone Name should not be blank")
 private String zoneName;
  @Min(1)
 private int pincode;
  @NotBlank
  private String area;
}
