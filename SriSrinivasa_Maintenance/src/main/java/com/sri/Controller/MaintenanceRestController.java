package com.sri.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sri.Entity.BreakDown;
import com.sri.Exception.RecordNotFoundException;
import com.sri.Service.EquipmentService;
import com.sri.Service.MaintenanceService;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceRestController {
	
	@Autowired
	EquipmentService equipmentService;
	
	@Autowired
	MaintenanceService maintenanceService;

	@GetMapping("/EquipmentExistById/{equipmentId}")
	public ResponseEntity<?> EquipmentExistById(@PathVariable("equipmentId") Long equipmentId){
		try {
			equipmentService.getMaintenanceFrequencyById(equipmentId);
			return ResponseEntity.ok().build();
		}catch(RecordNotFoundException e) {
			return ResponseEntity.notFound().build();
		}catch (Exception e) {
			return	ResponseEntity.internalServerError().build();
		}
	}
	
	@PostMapping("/BreakDown")
	public ResponseEntity<?> registerBreakDown(@RequestBody BreakDown breakDown){
		try {
			breakDown.setStatus("Created");
 	    	maintenanceService.saveBreakDown(breakDown);
 	    	return ResponseEntity.ok().build();
		}catch(Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
}
