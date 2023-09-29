package com.lancesoft.omg.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lancesoft.omg.entity.DeliveryBoyEntity;

public interface DeliveryBoyService {
	public DeliveryBoyEntity saveAgent(DeliveryBoyEntity deliveryBoyEntity, MultipartFile file, List<MultipartFile> file2,MultipartFile file3)  throws IOException;
   List<DeliveryBoyEntity>getAgent();
 void delAgent(Integer dAgentid,String userName, DeliveryBoyEntity deliveryBoyEntity);
}
