package com.lancesoft.omg.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lancesoft.omg.entity.DeliveryBoyEntity;
import com.lancesoft.omg.service.DeliveryBoyServiceImpl;
import com.lancesoft.omg.service.AdminRegistrationServiceImpl;
import com.lancesoft.omg.service.DeliveryBoyService;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")

public class DeliveryBoyController {
@Autowired
DeliveryBoyService deliveryBoyServiceImpl;
@Autowired
AdminRegistrationServiceImpl adminRegistrationServiceImpl;

@PostMapping(value = "/save/Agent")
public DeliveryBoyEntity saveAgent(@RequestParam String addingAgentEntity,@RequestParam MultipartFile file, @RequestParam List<MultipartFile> file2 ,@RequestParam MultipartFile img) throws IOException {
	
      ObjectMapper objectMapper = new ObjectMapper();
      DeliveryBoyEntity addingEntity=objectMapper.readValue(addingAgentEntity, DeliveryBoyEntity.class);
    return deliveryBoyServiceImpl.saveAgent(addingEntity, file, file2,img);
}
@GetMapping("/getAgent")
public ResponseEntity<DeliveryBoyEntity> getAgents() {



    return new ResponseEntity(deliveryBoyServiceImpl.getAgent(), HttpStatus.OK);
}
@DeleteMapping("/deleteAgent")
public ResponseEntity<DeliveryBoyEntity> deleteAgent(@RequestParam Integer dAgentid,HttpServletRequest httpServletRequest,DeliveryBoyEntity deliveryBoyEntity)
{
	String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);
	
  deliveryBoyServiceImpl.delAgent(dAgentid, userName, deliveryBoyEntity);
  return new ResponseEntity("Agent Deleted sucessfully",HttpStatus.OK);
}
}
