package com.lancesoft.omg.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.Locations;
import com.lancesoft.omg.entity.Zone;

public interface ZoneDao  extends JpaRepository<Zone, Integer>{

	Boolean existsByZoneName(String zoneName);
	
	

    

	



	
 
}
