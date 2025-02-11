package com.sri.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("SriSrinivasaUser")
public interface Employee_Client {

	@GetMapping("/employee/empEmailListByRole")
	ResponseEntity<?> getEmployeesBasedonRole(@RequestParam("role") String role,@RequestParam("reqUsername") String  reqUsername);

	@GetMapping("/employee/empRolesList")
	ResponseEntity<List<String>> getEmployeesAllRolesList();
	
	@GetMapping("/employee/empMailandSpecializationList/{role}")
	ResponseEntity<?> getEmployeemailandCapabilities(@PathVariable("role") String role);
	
	@GetMapping("/employee/getEmpByEmail/{email}")
	ResponseEntity<?> getEmployeeBasedOnEmail(@PathVariable("email") String email);
	
	@PostMapping("/employee/getEmpListbyEmailList")
	ResponseEntity<?> getEmpListbyEmailList(@RequestParam("emailList") List<String> emailList);
	
	@PostMapping("/employee/updateEmpRegisteredTeam/{registeredTeam}/{email}")
	ResponseEntity<?> updateEmployeeRegisteredTeam(@PathVariable("registeredTeam") String registeredTeam,@PathVariable("email") String email);
	
	@PostMapping("/employee/updateRemoveEmpRegisteredTeam/{email}")
	ResponseEntity<?> updateRemoveEmployeeRegisteredTeam(@PathVariable("email") String email);

}
