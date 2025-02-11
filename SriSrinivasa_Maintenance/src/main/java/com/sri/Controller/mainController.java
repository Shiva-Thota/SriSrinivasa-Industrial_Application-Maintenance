package com.sri.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maintenance")
public class mainController {

	
	@GetMapping("/")
	public String getDashboard() {
		return "Dashboard";
	}
	
	@GetMapping("/equipmentDashboard")
	public String getEquipmentDashboard() {
		return "EquipmentDashboard";
	}
	
	@GetMapping("/maintenanceDashboard")
	public String getMaintenanceDashboard() {
		return "MaintenanceDashboard";
	}

	@GetMapping("/error-403")
	public String getErrorPage() {
		return "error-403";
	}
}

