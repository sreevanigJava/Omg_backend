package com.lancesoft.omg.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.OrdersEntity;

public interface OrdersDao extends JpaRepository<OrdersEntity, String> {

	OrdersEntity findByUserName(String userName);
	OrdersEntity findByOrderId(String orderId);
	boolean existsByOrderId(String orderId);
	List<OrdersEntity> findByOrderDate(LocalDate orderDate);

	
}
