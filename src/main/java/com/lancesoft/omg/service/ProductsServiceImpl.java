package com.lancesoft.omg.service;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.AdminDashBoardDao;
import com.lancesoft.omg.dao.DeliveryBoyDao;
import com.lancesoft.omg.dao.DispatcherDao;
import com.lancesoft.omg.dao.LocationsDao;
import com.lancesoft.omg.dao.OrdersDao;
import com.lancesoft.omg.dao.PackingDepartDao;
import com.lancesoft.omg.dao.PinsDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.dao.ZoneDao;

import com.lancesoft.omg.dto.ProductsDto;

import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.DeliveryBoyEntity;
import com.lancesoft.omg.entity.Dispatcher;
import com.lancesoft.omg.entity.Locations;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.PackingDepart;

import com.lancesoft.omg.entity.Pins;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.entity.Zone;

import com.lancesoft.omg.exception.CustomException;
import com.lancesoft.omg.exception.LocationAlreadyExist;

import com.lancesoft.omg.exception.ProductAlreadyExist;
import com.lancesoft.omg.exception.ProductsAreEmpty;
import com.lancesoft.omg.exception.ZoneAlreadyExistException;

@Service
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	private PinsDao pinsDao;
	@Autowired
	private ZoneDao zoneDao;
	@Autowired
	private LocationsDao locationsDao;
	@Autowired
	private ProductsDao productsDao;
	@Autowired
	private DispatcherDao dispatcherDao;

	@Autowired
	private OrdersDao orderDao;
	@Autowired
	private PackingDepartDao packingDepartDao;
	@Autowired
	private AdminDashBoardDao adminDashBoardDao;
	@Autowired
	DeliveryBoyDao deliveryBoyDao;

	private static Logger logger = Logger.getLogger(ProductsServiceImpl.class);

	@Override
	public ProductsEntity addProducts(ProductsDto productsDto) throws ProductAlreadyExist {
		
		logger.info("Add products method start..");
		
		long purchasePrice=productsDto.getActualPrice()-(productsDto.getActualPrice()*productsDto.getOffer()/100);
		
		ModelMapper modelMapper = new ModelMapper();
		ProductsEntity productsEntity = new ProductsEntity();
		System.err.println(productsDto);
		if (productsDto == null) {
			throw new RuntimeException("null found in Products please check");
		} else if (productsDao.existsByProdName(productsDto.getProdName())) {
			throw new ProductAlreadyExist("Product Already exists");
		}

		modelMapper.map(productsDto, productsEntity);

		CategoriesEntity categoriesEntity = adminDashBoardDao
				.findByCatName(productsEntity.getCategoriesEntity().getCatName());
		productsEntity.setCategoriesEntity(categoriesEntity);
		productsEntity.setPurchasePrice(purchasePrice);
		logger.info("End of add products..");
		return productsDao.save(productsEntity);
		
	}

	public List<ProductsEntity> getAllProducts() {
		logger.info("Get all products start..");
		List<ProductsEntity> products = productsDao.findAll();

		if (!products.isEmpty()) {
			logger.info("Get all products end..");
			return products;
		} else
			throw new ProductsAreEmpty("Products are empty ");

	}

	public List<ProductsEntity> getCategory(String catName) {
		logger.info("Start of get Category method..");
		CategoriesEntity categoriesEntity = adminDashBoardDao.findByCatName(catName);
		String catId = categoriesEntity.getCatId();
		List<ProductsEntity> productsEntity = productsDao.findByCategoriesEntity(categoriesEntity);
		if (!productsEntity.isEmpty()) {
			logger.info("End of get category method..");
			return productsEntity;
		} else
			throw new ProductsAreEmpty("Given category products are not available");

	}

	public ProductsEntity findProdByName(String prodName) {
		if (!(productsDao.existsByProdName(prodName))) {
			throw new CustomException("Product does not exists");
		}
		ProductsEntity productsEntity = productsDao.findByProdName(prodName);

		return productsEntity;

	}

	@Override
	public ProductsEntity updateProd(ProductsEntity productsEntity) {

		if (productsEntity == null) {
			throw new CustomException("Products details should not be null");
		}

		return productsDao.save(productsEntity);
	}

	@Override
	public List<OrdersEntity> getALlorders() {

		logger.info("Get all orders start..");
		List<OrdersEntity> orders = orderDao.findAll();

		if (!orders.isEmpty()) {
			logger.info("Get all orders end..");
			return orders;
		} else
			throw new CustomException("orders are empty ");
	}

	public List<Object> getOrderByDate(LocalDate localDate, PackingDepart abc) {
		/*
		 * private int totalOrders; private int pendingOrders; private int packed;
		 */
		int totalOrders = abc.getTotalOrders();
		int pendingOrders = abc.getPendingOrders();
		int packed = abc.getPacked();
		List<OrdersEntity> orders = orderDao.findByOrderDate(localDate);

		System.out.println("GET ORDERS BY DATE IS CALled?>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		for (int i = 0; i < orders.size(); i++) {
			totalOrders++;

		}
		pendingOrders = totalOrders;

		System.out.println(totalOrders + "----" + packed + ">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		PackingDepart packingDepart = new PackingDepart(totalOrders, packed, pendingOrders);
		System.out.println(packingDepart.getTotalOrders());
		packingDepart.setTotalOrders(totalOrders);
		packingDepart.setPendingOrders(pendingOrders);
		packingDepart.setOrderedDate(localDate);
		packingDepartDao.save(packingDepart);
		ArrayList<Object> list = new ArrayList<>();
		list.add(orders);
		list.add(packingDepart);
		if (!orders.isEmpty()) {
			return list;
		} else
			throw new CustomException("orders are empty ");

	}

	@Override
	public PackingDepart ordersPacked(Boolean isPacked, LocalDate datee) {

		PackingDepart packingDepart = packingDepartDao.findByorderedDate(datee);
		int totalOrders = packingDepart.getTotalOrders();
		int pendingOrders = packingDepart.getPendingOrders();
		int packed = packingDepart.getPacked();

		if (isPacked == true && !(pendingOrders == 0)) {

			packed = packed + 1;
			pendingOrders = pendingOrders - 1;

		} else
			throw new CustomException("There is no pending for packing");
		packingDepart.setPacked(packed);
		packingDepart.setPendingOrders(pendingOrders);

		return packingDepartDao.save(packingDepart);
	}

	@Override
	public Locations addLocation(Locations location) {

		if (locationsDao.existsByLocationName(location.getLocationName())) {
			throw new LocationAlreadyExist("Location Already Exist");
		}
		return locationsDao.save(location);

	}

	@Override
	public List<Locations> gellAllLocations() {

		List<Locations> local = locationsDao.findAll();
		return local;
	}

	@Override

	public Zone addZone(Zone zone) {
		if (zoneDao.existsByZoneName(zone.getZoneName())) {
			throw new ZoneAlreadyExistException("Zone AlreadyExist Exception");
		} else
			return zoneDao.save(zone);

	}

	@Override
	public Pins addPinAndArea(Pins pins) {
		if (pinsDao.existsByarea(pins.getArea()) || pinsDao.existsBypincode(pins.getPincode()))

		{
			throw new CustomException("PinCode(or)Area Already Exist");
		} else
			return pinsDao.save(pins);

	}

	@Override
	public List<Pins> getList(String locationName) {

		List<Pins> list1 = pinsDao.findByLocationName(locationName);

		return list1;
	}

	@Override
	public Dispatcher dispatched(Dispatcher dispatcher) {

		return dispatcherDao.save(dispatcher);
	}

	@Override
	public List<Dispatcher> dispatcher(String orderDate) {
		List<Dispatcher> list2 = dispatcherDao.findByOrderDate(orderDate);
		return list2;
	}

	@Override
	public OrdersEntity getorderid(String orderId) {

		return orderDao.findByOrderId(orderId);
	}

	@Override
	public List<DeliveryBoyEntity> getagents(String area) {

		List<DeliveryBoyEntity> agents = new ArrayList<>();

		List<DeliveryBoyEntity> agent = deliveryBoyDao.findByarea(area);

		for (int i = 0; i < agent.size(); i++) {
			DeliveryBoyEntity delivery = agent.get(i);

			if (delivery.getStatus().equals("avilable")) {
				agents.add(delivery);
			}
		}
		return agents;
	}
}