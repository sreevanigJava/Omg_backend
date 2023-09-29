package com.lancesoft.omg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.Pins;

public interface PinsDao extends JpaRepository<Pins, Integer> {
  Boolean existsBypincode(Integer pincode);
  Boolean existsByarea(String area);
  List<Pins> findByLocationName(String locationName);
}
