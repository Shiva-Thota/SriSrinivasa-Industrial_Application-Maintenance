package com.sri.Scheduling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sri.Entity.Equipment;
import com.sri.Entity.MaintenanceTask;
import com.sri.Service.MaintenanceService;

@Component
public class MaintenanceEqpmtSchdlng {

	@Autowired
	MaintenanceService maintenanceService;
		//	@Scheduled(cron = "0 37 9 * * 1-6")

	@Scheduled(cron = "0 57 8 * * *")
	public void getMaintenanceEquipmentListToday() {
 		List<Equipment> list=maintenanceService.getTodaysMaintenanceEquipmentList();
		list.forEach(eq->{
			MaintenanceTask task=new MaintenanceTask();
			task.setCriticalityLevel(eq.getCriticalityLevel());
			task.setEquipmentId(eq.getEquipmentId());
			task.setScheduledDate(eq.getNextMaintenanceDate());
			task.setStatus("Pending");
			task.setMaintenanceFrequency(eq.getMaintenanceFrequency());
			task.setLocation(eq.getLocation());
			maintenanceService.saveMaintenanceTask(task);
		});
 	}
	
	
}
