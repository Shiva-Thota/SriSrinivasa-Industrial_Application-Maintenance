package com.sri.Messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.sri.DTO.Mntnc_Inv_DTO_Message_Product;
import com.sri.DTO.New_Product_Req;

@Service
public class MessagePublisher {
	
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	public void MessageForInventoryMaterial(Mntnc_Inv_DTO_Message_Product prodList) {
		System.out.println("MessagePublisher.MessageForInventoryMaterial()");
		Message<Mntnc_Inv_DTO_Message_Product> message=MessageBuilder.withPayload(prodList)
				.setHeader(KafkaHeaders.TOPIC, "inventory-manager-MaintenanceMaterial")
				.setHeader("actionType","maintenanceMaterial")
				.build();
		kafkaTemplate.send(message);
	}
	
	public void MessageCriticalBreakdown(Long equipmentId,String failureMessage,Long breakDownId) {
		System.out.println("MessagePublisher.MessageCriticalBreakdown()");
		System.out.println("Message to Maintenance Manager");
	}
	
	public void MessageTechnician(Long workOrderId,String technicianId) {
		System.out.println("MessagePublisher.MessageTechnician()");
		System.out.println(workOrderId+"-------------------"+technicianId);
	}
	
	public void MessageForNewProduct(New_Product_Req req) {
 		Message<New_Product_Req> message=MessageBuilder.withPayload(req)
				.setHeader(KafkaHeaders.TOPIC, "inventory-manager-NewMaterialRequest")
				.setHeader("actionType","newMaterialRequest")
				.build();
		kafkaTemplate.send(message);
	}
	 
}
