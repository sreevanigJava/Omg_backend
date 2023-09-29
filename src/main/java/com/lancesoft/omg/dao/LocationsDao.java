package com.lancesoft.omg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.Locations;

public interface LocationsDao extends JpaRepository<Locations, Integer> {
    Boolean existsByLocationName(String locationName);
    

}
