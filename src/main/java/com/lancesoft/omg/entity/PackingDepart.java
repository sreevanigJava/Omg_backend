package com.lancesoft.omg.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="packingDepart")
public class PackingDepart {
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	 private int id;
	private LocalDate orderedDate;
	    private int totalOrders;
	    private int packed;
	    private int pendingOrders;
	    public PackingDepart(int totalOrders, int packed, int pendingOrders) {
	        super();
	        this.totalOrders = totalOrders;
	        this.packed = packed;
	        this.pendingOrders = pendingOrders;
	    }
		public PackingDepart() {
			super();
			// TODO Auto-generated constructor stub
		}
		
	    
}
