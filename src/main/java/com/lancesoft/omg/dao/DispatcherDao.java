package com.lancesoft.omg.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.Dispatcher;

public interface DispatcherDao extends JpaRepository<Dispatcher, Integer> {
    List<Dispatcher> findByOrderDate(String orderDate);
}
