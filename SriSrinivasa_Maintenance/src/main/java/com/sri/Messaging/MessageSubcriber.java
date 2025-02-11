package com.sri.Messaging;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.sri.DTO.New_Product_Req;
import com.sri.Service.MaintenanceService;

@Service
public class MessageSubcriber {

	@Autowired
	MaintenanceService maintenanceService;
	
	 
	@KafkaListener(topics = "maintenance-manager-topic-WorkOrderInvntry",groupId = "maintenance")
	public void productionOrderMessage(Message<Object> message, @Header("actionType") String actionType) {	
		System.out.println("--------------------------------****************************************** MEssage Received");
		
		try {
			Map<String,Object> msg=(Map<String,Object>) message.getPayload();
			System.out.println(msg);
			Long workOrderId=Long.valueOf(msg.get("workOrderId").toString());
			String response=(String) msg.get("response");
			System.out.println(workOrderId+"                 -             "+actionType);
			switch(actionType) {
				case "WorkOrderInventoryApproved":
					System.out.println("-------------------------------------------------");
					maintenanceService.updateWorkOrderStatusbyId("Inventory Approved",workOrderId);
					break;
 				case "WorkOrderInventoryRejected":
					maintenanceService.updateWorkOrderStatusbyId("Inventory Rejected", workOrderId);
				break;
			}
		}catch(Exception e) {
			System.out.println(e);
		}	
	}
	
	 @KafkaListener(topics="maintenance-manager-topic-newProdReq",groupId = "maintenance")
    public void messageFromNewProduct(Message<New_Product_Req> message) {
    	try {
    		System.out.println("New Product Approved-----------------------------------");
    		New_Product_Req req=message.getPayload();
    		maintenanceService.addNew_Product_Req(req);
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
	 
	 @KafkaListener(topics="maintenance-employee-delete",groupId = "maintenance")
	    public void messageFromUserManagement(Message<String> message) {
	    	try {
	    		System.out.println("Employeee going to Deleted-----------------------------------");
	    		String email=message.getPayload();
	    		maintenanceService.deleteEmployeeFromTeams(email);
	    		System.out.println("Employeee Deleted-----------------------------------   "+email);
	    	}catch(Exception e) {
	    		System.out.println(e);
	    	}
	    }
	
}
