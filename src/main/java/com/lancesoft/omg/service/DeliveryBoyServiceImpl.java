package com.lancesoft.omg.service;

import java.io.IOException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import com.lancesoft.omg.dao.DeliveryBoyDao;
import com.lancesoft.omg.entity.DeliveryBoyEntity;
import com.lancesoft.omg.entity.Vaccination;
import com.lancesoft.omg.exception.CustomException;

@Service
public class DeliveryBoyServiceImpl implements DeliveryBoyService {
	@Autowired
	DeliveryBoyDao deliveryBoyDao;
	private static Logger logger = Logger.getLogger(DeliveryBoyServiceImpl.class);
    @Override
    public DeliveryBoyEntity saveAgent(DeliveryBoyEntity deliveryBoyEntity, MultipartFile file, List<MultipartFile> file2 ,MultipartFile img)
            throws IOException {

       if(deliveryBoyDao.existsByPhoneNumber(deliveryBoyEntity.getPhoneNumber()))
       {
    	   throw new CustomException("Agent Already Exsist");
       }
    	deliveryBoyEntity.setDrivinglicenseDoc(file.getBytes());

    	deliveryBoyEntity.setAgentImage(img.getBytes());
    	deliveryBoyEntity.setArea(deliveryBoyEntity.getArea());
    	deliveryBoyEntity.setStatus(null);
        List<Vaccination> listOfVcRd = deliveryBoyEntity.getVac();

 

        if(listOfVcRd.size()==file2.size())
        {
        for (int i = 0; i < listOfVcRd.size(); i++) {

            listOfVcRd.get(i).setVaccinationcertificate(file2.get(i).getBytes());

 

        }
        }else
            throw new CustomException("please upload "+listOfVcRd.size()+" files only");
      	

        return deliveryBoyDao.save(deliveryBoyEntity);
    }
	
	@Override
	public List<DeliveryBoyEntity> getAgent() {
		
		return deliveryBoyDao.findAll();
	}

	@Override
	public void delAgent(Integer dAgentid, String userName, DeliveryBoyEntity deliveryBoyEntity) {
		
		logger.info("Delete cart item start..");
		if(!(deliveryBoyDao.existsBydAgentid(dAgentid)))
		{
			throw new CustomException("Could not delete as it does not exists");
		}
		DeliveryBoyEntity add = deliveryBoyDao.findBydAgentid(dAgentid);

		deliveryBoyDao.delete(add);
		
	}
}
