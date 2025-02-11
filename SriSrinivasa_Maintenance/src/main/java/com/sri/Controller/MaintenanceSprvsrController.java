package com.sri.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.DTO.Prod_User_DTO_Employee;
import com.sri.Entity.MaintenanceTeam;
import com.sri.Entity.WorkOrder;
import com.sri.Exception.RecordNotFoundException;
import com.sri.FeignClient.Employee_Client;
import com.sri.Service.MaintenanceService;
import com.sri.utils.EquipmetUtils;

import feign.FeignException;

@Controller
@RequestMapping("/maintenance/sprvsr")
public class MaintenanceSprvsrController {

	@Value("${gateway.url}")
	String gateWayUrl;
	
	@Autowired
	Employee_Client employee_Client;
	
	@Autowired
	MaintenanceService maintenanceService;
	
	@Autowired
	ObjectMapper objectMapper; 
	
	@GetMapping("/assignSprvsr/{workOrderId}")
	public String getAssignSupervisorPage(@PathVariable("workOrderId") Long workOrderId,Map<String,Object> map) {
 		try {
 			List<String> supervisorList=maintenanceService.getMaintenanceTeamSupervisorList();
 			
			WorkOrder workOrder=maintenanceService.getWorkOrderById(workOrderId);
			map.put("workOrder", workOrder);
			map.put("supervisorList", supervisorList);
		} catch(RecordNotFoundException e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
		return "WorkOrderDetails";
	}
	
//	@GetMapping("/assignSprvsr/{workOrderId}")
//	public String getWorkOrderDetails(@PathVariable("workOrderId") Long workOrderId,Map<String,Object> map) {
//		try {
//			WorkOrder workOrder=maintenanceService.getWorkOrderById(workOrderId);
//			map.put("workOrder", workOrder);
// 		} catch (RecordNotFoundException e) {
//			e.printStackTrace();
//			return "ErrorInternalServer";
//		}
//		return "WorkOrderDetails";
//	}
	
	@PostMapping("/assignSprvsr")
	public String assignProdSupervisor(@RequestParam("supervisorEmail") String email, @RequestParam("workOrderId") Long workOrderId,RedirectAttributes redirectAttributes) {
		String oldSupervisor=maintenanceService.getWorkOrderMaintenanceTeamSupervisor(workOrderId);
		try {
			if(oldSupervisor!=null)
				maintenanceService.deleteMainteneceTeamWorkOrderId(oldSupervisor, workOrderId);
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
		}
		maintenanceService.updateWorkOrderMaintenanceTeamSupervisor(email, workOrderId);
			try {
				maintenanceService.addMainteneceTeamWorkOrderId(email, workOrderId);
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("Team_Not_Exist","Team Doesn't Exist for Supervisor"+email);
			}
		return "redirect:"+gateWayUrl+"/maintenance/mntnce/getWorkOrder/"+workOrderId;
	}
	
	@GetMapping("/viewTeam/{superviser}")
	public String getProductionTeam(@PathVariable("superviser") String superviser,Map<String,Object> map,Principal principal,Authentication authentication) {
		if(EquipmetUtils.hasRole(authentication, "MAINTENANCE_SUPERVISOR")) {
			String username=principal.getName();
			if(!username.equalsIgnoreCase(superviser))
				return "error-403";
		}
		try{
			MaintenanceTeam maintenanceTeam = maintenanceService.getMaintenanceTeamBySupervisor(superviser);
			List<Long> WorkOrders=maintenanceTeam.getWorkOrderIds();
			List<Long> previousWorkOrders=new ArrayList<Long>();
			List<Long> currentWorkOrders=new ArrayList<Long>();
			
			WorkOrders.forEach(ordrId->{
				try {
					if(!maintenanceService.getMaintenanceStatusbyId(ordrId).equalsIgnoreCase("Completed")) {
						currentWorkOrders.add(ordrId);
					}else {
						previousWorkOrders.add(ordrId);
					}	
				}catch(NullPointerException e) {
					
				}
			});		
			
			try{
				ResponseEntity<?> responseTeamList= employee_Client.getEmpListbyEmailList(maintenanceTeam.getTeamMates());
				if(responseTeamList.getStatusCode().isError()) {
					return "ErrorInternalServer";
				}
				List<Prod_User_DTO_Employee> teamMatesList=(List<Prod_User_DTO_Employee>) responseTeamList.getBody();
				
				ResponseEntity<?> responseSupervisor=employee_Client.getEmployeeBasedOnEmail(superviser);
				if(responseSupervisor.getStatusCode().isError()) {
					return "ErrorInternalServer";
				}
				List<?> supervisorListResponse=(List<?>)  responseSupervisor.getBody();
				if(!supervisorListResponse.isEmpty()) {
					List<Prod_User_DTO_Employee> supervisorList=supervisorListResponse.stream().map(spvsr->objectMapper.convertValue(spvsr,Prod_User_DTO_Employee.class)).collect(Collectors.toList());
					 Prod_User_DTO_Employee supervisorDetails = supervisorList.get(0);
					 if(supervisorDetails.getPhoto()!=null)
						 map.put("supervisorPhoto", Base64.getEncoder().encodeToString(supervisorDetails.getPhoto()));
					 supervisorDetails.setPhoto(null);
					 map.put("supervisorDetails", supervisorDetails);
				}
				map.put("TeamMatesList", teamMatesList);
			}catch(Exception e) {
				e.printStackTrace();
				return "ErrorInternalServer";
			}
			map.put("currentWorkOrders", currentWorkOrders);
			map.put("previousWorkOrders", previousWorkOrders);
			map.put("maintenanceTeam", maintenanceTeam);
			return "MntnceTeamDetails";
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
	}
	
	@GetMapping("/team/create")
	public String getProductionTeamCreationPage(Principal principal,Map<String,Object> map) {
		try {
			 
			ResponseEntity<?> response= employee_Client.getEmployeemailandCapabilities("MAINTENANCE_SUPERVISOR");
			if(response.getStatusCode().isError()) {
				return "ErrorInternalServer";
			}
			Map<String,String> supervisorList=  (Map<String,String>) response.getBody();
 			map.put("supervisorList", supervisorList);
			return "mntnceTeamRegister";
		}catch(Exception e) {
 			return "ErrorInternalServer";
		}
	}
	
	@PostMapping("/team/create")
	public String registerProductionTeam(@RequestParam("supervisorEmail") String supervisorEmail,RedirectAttributes redirectAttributes,Principal principal) {
		try {
			maintenanceService.getMaintenanceTeamBySupervisor(supervisorEmail);
			redirectAttributes.addFlashAttribute("Team_already_Exist","Team Already Exixt for Supervisor"+supervisorEmail);
		} catch (RecordNotFoundException e) {
			MaintenanceTeam team=new MaintenanceTeam();
			team.setSupervisor(supervisorEmail);
			team.setManager(principal.getName());
			team.setStatus("Active");
			maintenanceService.addMaintenanceTeam(team);
			return "redirect:"+gateWayUrl+"/maintenance/sprvsr/viewTeam/"+supervisorEmail;
		}
		return "redirect:"+gateWayUrl+"/maintenance/sprvsr/team/create";
	}
	
	@PostMapping("/team/addTeammate")
	public String addNewTeamMates(@RequestParam("supervisor") String supervisor,@RequestParam("teammate") String teammate,RedirectAttributes redirectAttributes) {
		try {
			ResponseEntity<?> responseSupervisor=employee_Client.getEmployeeBasedOnEmail(teammate);
			if(responseSupervisor.getStatusCode().isError()) {
				return "ErrorInternalServer";
			}
			List<?> supervisorListResponse=(List<?>)  responseSupervisor.getBody();
		    if(!supervisorListResponse.isEmpty()) {
		    	String RegisteredTeamName=maintenanceService.EmployeeRegisteredInAnyTeam(teammate);
		    	if(RegisteredTeamName==null) {
		    		//Adding teammate to the Team
		    		maintenanceService.addMainteneceTeamTeammate(supervisor, teammate);
		    		//Adding Team name to the employee RegisteredTeam 
		    		employee_Client.updateEmployeeRegisteredTeam(supervisor, teammate);
		    		
		    	}else {
			    	redirectAttributes.addFlashAttribute("Employee_Registered_Another_Team","Employyee with Id:"+teammate+" Registered in Team :"+RegisteredTeamName);
		    	}
		    }else {
		    	redirectAttributes.addFlashAttribute("Employee_Not_Exist","Employyee with Id:"+teammate+" Not Found in Databse");
		    }
		    
			return "redirect:"+gateWayUrl+"/maintenance/sprvsr/viewTeam/"+supervisor;
		}catch(FeignException ex) {
	    	redirectAttributes.addFlashAttribute("Employee_Not_Exist","Employyee with Id:"+teammate+" Not Found in Databse");
	    	return "redirect:"+gateWayUrl+"/maintenance/sprvsr/viewTeam/"+supervisor;
		}catch (Exception e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
	}
	@PostMapping("/team/dlteTeammate")
	public String dlteTeamMates(@RequestParam("supervisor") String supervisor,@RequestParam("teammate") String teammate){
		try {
			maintenanceService.deleteMainteneceTeamTeammate(supervisor, teammate);
			//removing Team name to the employee RegisteredTeam 
    		employee_Client.updateRemoveEmployeeRegisteredTeam(teammate);
			return "redirect:"+gateWayUrl+"/maintenance/sprvsr/viewTeam/"+supervisor;
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
	}
	@PostMapping("/viewTeammatePage")
	public String addTeammatePage(@RequestParam("supervisor") String supervisor,@RequestParam("teammate") String teammate,@RequestParam("operation") String operation,RedirectAttributes redirectAttributes
									,Map<String,Object> map,Principal principal,Authentication authentication) {
		
		if(EquipmetUtils.hasRole(authentication, "MAINTENANCE_SUPERVISOR")) {
			String username=principal.getName();
			if(!username.equalsIgnoreCase(supervisor))
				return "error-403";
		}
		
		map.put("supervisor", supervisor);
		map.put("teammate", teammate);
		map.put("operation", operation);
		try {
			try{
				ResponseEntity<?> response= employee_Client.getEmployeeBasedOnEmail(teammate);
				if(response.getStatusCode().isError()) {
					return "ErrorInternalServer";
				}
				List<?> teamMatesListResponse=(List<?>)  response.getBody();
				if(!teamMatesListResponse.isEmpty()) {
					List<Prod_User_DTO_Employee> teamMatesList=teamMatesListResponse.stream().map(spvsr->objectMapper.convertValue(spvsr,Prod_User_DTO_Employee.class)).collect(Collectors.toList());
					 Prod_User_DTO_Employee emp = teamMatesList.get(0);
					if(emp.getPhoto()!=null){ 
						map.put("EmployeePhoto", Base64.getEncoder().encodeToString(emp.getPhoto()));
					}
					 emp.setPhoto(null);
					 map.put("EmployeeProfile", emp);
				}
			}catch(FeignException ex) {
		    	redirectAttributes.addFlashAttribute("Employee_Not_Exist","Employyee with Id:"+teammate+" Not Found in Databse");
		    	return "redirect:"+gateWayUrl+"/maintenance/sprvsr/viewTeam/"+supervisor;
			}catch(Exception e) {
				e.printStackTrace();
				return "ErrorInternalServer";
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
		return "employeeDetails";
	}
	
	@GetMapping("/team/all")
	public String getAllProductionTeams(Map<String,Object> map) {
		map.put("maintenanceTeam",maintenanceService.getAllMaintenanceTeamsList());
		return "MntnceTeamList";
	}
	
	@PostMapping("/feedbackAndStatus")
	public String changeStatusAndFeedbackOfWorkOrder(@RequestParam("workOrderId") Long workOrderId,@RequestParam("technicianFeedback") String technicianFeedback,@RequestParam("status") String status) {
		try {
			maintenanceService.updateWorkOrderStatusbyId(status, workOrderId);
			maintenanceService.updateWorkOrderTechnicianFeedbackbyId(technicianFeedback, workOrderId);
		} catch (RecordNotFoundException e) {
 			e.printStackTrace();
		}
		return "redirect:"+gateWayUrl+"/maintenance/mntnce/getWorkOrder/"+workOrderId;
	}
	
	@GetMapping("/empList")
	public String getEmployeeDetails(@RequestParam(name="role",required = false) String role,Map<String,Object> map,Principal principal) {
		try {
			if(role==null||role.isBlank())
				role="MAINTENANCE_MANAGER";
 			ResponseEntity<?> empListResponse=employee_Client.getEmployeesBasedonRole(role,principal.getName());
 			List<?> empList=(List<?>)  empListResponse.getBody();
 			if(!empList.isEmpty()) {
				List<Prod_User_DTO_Employee> empListdto=empList.stream().map(spvsr->objectMapper.convertValue(spvsr,Prod_User_DTO_Employee.class)).collect(Collectors.toList());
 				 map.put("EmployeesList", empListdto);
			}
 			ResponseEntity<List<String>> rolesList=employee_Client.getEmployeesAllRolesList();
 			map.put("Roles", rolesList.getBody());
 			map.put("selectedRole", role);
 			map.put("loggedInUser",principal.getName());
 		}catch(Exception e) {
 			e.printStackTrace();
			return "ErrorInternalServer";
		}
		return "EmployeeList";
	}	
	
	@PostMapping("/deleteTeam")
	public String deleteTeam(@RequestParam("supervisor") String supervisor){
		List<String> teammateList=maintenanceService.getMaintenanceTeamTeammatesList(supervisor);
		teammateList.forEach(teammate->{
			employee_Client.updateRemoveEmployeeRegisteredTeam(teammate);
		});
		 
		maintenanceService.deleteMaintenanceTeam(supervisor);
			return "redirect:"+gateWayUrl+"/maintenance/sprvsr/team/all";
	}
}














