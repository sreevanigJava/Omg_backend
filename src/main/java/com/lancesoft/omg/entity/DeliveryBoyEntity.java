package com.lancesoft.omg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="addingAgentEntity")
public class DeliveryBoyEntity {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
  private int dAgentid;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String alternate_PhoneNumber;
  @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  private D_AgentAdress address;
  @Lob
  private byte[] drivinglicenseDoc;
  private String drivingLicenseNumber;
  @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  private List<Vaccination> vac;
  private byte[] agentImage;
  private String area;
  private String status;
}