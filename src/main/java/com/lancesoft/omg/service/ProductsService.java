package com.lancesoft.omg.service;



import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.tools.DocumentationTool.Location;

import org.springframework.http.HttpStatus;

import com.lancesoft.omg.dto.CategoriesDto;

import com.lancesoft.omg.dto.ProductsDto;
import com.lancesoft.omg.entity.DeliveryBoyEntity;
import com.lancesoft.omg.entity.Dispatcher;
import com.lancesoft.omg.entity.Locations;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.PackingDepart;

import com.lancesoft.omg.entity.Pins;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.entity.Zone;


public interface ProductsService {
	
	
	ProductsEntity addProducts(ProductsDto productsDto);
	List <ProductsEntity> getAllProducts();
	List<ProductsEntity> getCategory(String catName);
	ProductsEntity findProdByName(String prodName);
	ProductsEntity updateProd(ProductsEntity productsEntity);
	List<OrdersEntity> getALlorders();
	List<Object> getOrderByDate(LocalDate localDate, PackingDepart packingDepart);
	PackingDepart ordersPacked(Boolean isPacked,LocalDate datee);
	Locations addLocation(Locations location);
	List<Locations> gellAllLocations();
	Zone addZone(Zone zone);
	Pins addPinAndArea(Pins pins);
	List<Pins> getList(String locationName);
	Dispatcher dispatched(Dispatcher dispatcher);
	List<Dispatcher> dispatcher(String orderDate );
	OrdersEntity getorderid(String orderId);
	List<DeliveryBoyEntity> getagents(String area );
	
}
