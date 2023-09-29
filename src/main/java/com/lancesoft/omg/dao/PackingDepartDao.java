package com.lancesoft.omg.dao;



import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.lancesoft.omg.entity.PackingDepart;


public interface PackingDepartDao extends JpaRepository<PackingDepart, Integer> {
	PackingDepart findByorderedDate(LocalDate datee);
}
