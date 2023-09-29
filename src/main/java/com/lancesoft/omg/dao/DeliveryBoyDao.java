package com.lancesoft.omg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.DeliveryBoyEntity;

public interface DeliveryBoyDao extends JpaRepository<DeliveryBoyEntity, Integer>{
  DeliveryBoyEntity findBydAgentid(Integer dAgent_id);
  Boolean existsBydAgentid(Integer id);
  Boolean existsByPhoneNumber(String phoneNumber); 
  List<DeliveryBoyEntity> findByarea(String area);

}
