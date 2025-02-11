package com.sri.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sri.DTO.Mntnc_Inv_DTO_Message_Product;
import com.sri.DTO.New_Product_Req;
import com.sri.Entity.BreakDown;
import com.sri.Entity.Equipment;
import com.sri.Entity.MaintenanceTask;
import com.sri.Entity.MaintenanceTeam;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Entity.MaterialRequiredWorkOrder;
import com.sri.Entity.WorkOrder;
import com.sri.Exception.RecordNotFoundException;
import com.sri.Messaging.MessagePublisher;
import com.sri.Persistance.EquipmentDAO;
import com.sri.Persistance.MaintenanceDAO;
import com.sri.utils.EquipmetUtils;

import io.jsonwebtoken.lang.Collections;

@Service
public class MaintenanceServiceImpl implements MaintenanceService{

	@Autowired
	EquipmentDAO equipmentDAO;
	
	@Autowired
	MaintenanceDAO maintenanceDAO;
	
	
	@Autowired
	MessagePublisher messagePublisher;
	
	@Override
	public List<Equipment> getTodaysMaintenanceEquipmentList() {
		return equipmentDAO.getTodaysMaintenanceEquipmentList();
	}

	@Override
	public List<Equipment> findEquipmentForSpecifiedDays(LocalDate startDate, LocalDate endDate) {
		return equipmentDAO.findEquipmentForNextTwoDays(startDate, endDate);
	}
	@Override
	public List<Equipment> findEquipmentForNextTwoDays() {
		return equipmentDAO.findEquipmentForNextTwoDays(LocalDate.now(), LocalDate.now().plusDays(2));
	}

	@Override
	public int updateWorkOrderMaterialListbyId(List<MaterialRequiredWorkOrder> materialList, Long workOrderId) throws RecordNotFoundException {
		
		 WorkOrder order=maintenanceDAO.getWorkOrderById(workOrderId);
		 //setting the status of maintenance Task if it is preventive maintenance
		 if(order.getMaintenanceType().equalsIgnoreCase("Preventive")) {
			 try {
			 maintenanceDAO.updateMaintenanceTaskStatusbyId("Completed", order.getMaintenanceTaskId());
			 WorkOrder workOrder=maintenanceDAO.getWorkOrderById(workOrderId);
			 List<MaterialRequiredEquipment> maintenanceEqpmtMaterialsbyId = equipmentDAO.getMaintenanceMaterialsbyId(workOrder.getEquipmentId());
			List<MaterialRequiredWorkOrder> workOrderOldMaterialList=maintenanceEqpmtMaterialsbyId.stream().map(material->EquipmetUtils.getMaterialRequiredWorkOrder(material)).collect(Collectors.toList());
 			workOrderOldMaterialList.forEach(mat->{
				mat.setWorkOrder(workOrder);
  				 if(!materialList.contains(mat)) {
 					 materialList.add(mat);
  				 }
			 });
 			 }catch(Exception e) {}
 		 }else {
			 maintenanceDAO.updateBreakDownStatus("Completed", order.getBreakDownId());
			 maintenanceDAO.updateWorkOrderStatusbyId("Approved", workOrderId);
		 }
		 //updating the next maintenance date of equipment 
		 LocalDate nextUpdateDate=EquipmentServiceImpl.getNextMaintenanceDate(equipmentDAO.getMaintenanceFrequencyById(order.getEquipmentId()));
		 equipmentDAO.updateEquipmentnextMaintenanceDatebyId(nextUpdateDate, order.getEquipmentId());
		
		 //Sending Message to Inventory Department
		 Map<Long,Integer> materialQuantityMap=new HashMap<Long, Integer>();
		 for(MaterialRequiredWorkOrder workOrder: materialList) {
			 materialQuantityMap.put(workOrder.getProductId(), workOrder.getQuantityRequired());
		 }
		 Mntnc_Inv_DTO_Message_Product msg=new Mntnc_Inv_DTO_Message_Product();
		 msg.setEquipmentId(order.getEquipmentId());
		 msg.setEquipmentLocation(order.getLocation());
		 msg.setMaintenanceManagerComments(null);
		 msg.setMaintenanceManagerId(null);
		 msg.setMaterialandQuantity(materialQuantityMap);
		 msg.setWorkOrderId(workOrderId);
		 
		 messagePublisher.MessageForInventoryMaterial(msg);

		 //send message to technician
		 messagePublisher.MessageTechnician(workOrderId, order.getMaintenanceTeam());
 		return maintenanceDAO.updateWorkOrderMaterialListbyId(materialList, workOrderId);
	}

	@Override
	public int updateWorkOrderStatusbyId(String status, Long workOrderId) throws RecordNotFoundException {
 		return maintenanceDAO.updateWorkOrderStatusbyId(status, workOrderId);
	}

	@Override
	public int updateWorkOrderTechnicianFeedbackbyId(String technicianFeedback, Long workOrderId) throws RecordNotFoundException {
 		return maintenanceDAO.updateWorkOrderTechnicianFeedbackbyId(technicianFeedback, workOrderId);
	}

	@Override
	public MaintenanceTask getMaintenanceTaskbyId(Long maintenanceTaskId) throws RecordNotFoundException {
		 return maintenanceDAO.getMaintenanceTaskbyId(maintenanceTaskId);
	}

	@Override
	public String saveMaintenanceTask(MaintenanceTask maintenanceTask) {
 		return maintenanceDAO.saveMaintenanceTask(maintenanceTask);
	}

	@Override
	public List<MaintenanceTask> getMaintenanceTaskbyStatus(String status) {
		return maintenanceDAO.getMaintenanceTaskbyStatus(status);
	}

	@Override
	public List<MaintenanceTask> getAllMaintenanceTasks() {
		return maintenanceDAO.getAllMaintenanceTasks();
	}

	@Override
	public WorkOrder getWorkOrderById(Long workOrderId) throws RecordNotFoundException {
 		return maintenanceDAO.getWorkOrderById(workOrderId);
	}

	@Override
	public String saveWorkOrder(WorkOrder workOrder) {
 		return maintenanceDAO.saveWorkOrder(workOrder);
	}

	@Override
	public List<MaterialRequiredWorkOrder> getWorkOrderMaterialRequiredbyId(Long workOrderId) {
		return maintenanceDAO.getWorkOrderMaterialRequiredbyId(workOrderId);
	}

	@Override
	public int updateMaintenanceTaskStatusbyId(String status, Long maintenanceTaskId) {
		 
		return maintenanceDAO.updateMaintenanceTaskStatusbyId(status, maintenanceTaskId);
	}

	@Override
	public Page<WorkOrder> getWorkOrder(Long equipmentId, Long workOrderId, String maintenanceType,
	                                    String priorityLevel, String status, String technician, Pageable pg) {
	    Page<WorkOrder> page = null;
 
	    if (technician != null && !technician.isBlank()) {
	    	 if (workOrderId != null) {
	 	        page = maintenanceDAO.findByMaintenanceTeamAndWorkOrderId(technician, workOrderId, pg);
	  	    }else if (equipmentId != null) {
	 	        page = maintenanceDAO.findByMaintenanceTeamAndEquipmentId(technician, equipmentId, pg);
	  	    }else if (maintenanceType != null && !maintenanceType.isBlank()) {
	 	        page = maintenanceDAO.findByMaintenanceTeamAndMaintenanceType(technician, maintenanceType, pg);
	  	    }else if (priorityLevel != null && !priorityLevel.isBlank()) {
	 	        page = maintenanceDAO.findByMaintenanceTeamAndPriorityLevel(technician, priorityLevel, pg);
	  	    }else if (status != null && !status.isBlank()) {
	 	        page = maintenanceDAO.findByMaintenanceTeamAndStatus(technician, status, pg);
	  	    }else {
		        page = maintenanceDAO.findByTechnician(technician, pg);    
	  	    }	        
 	    }else if (workOrderId != null) {
	        page = maintenanceDAO.findByWorkOrderId(workOrderId, pg);
 	    }else if (equipmentId != null) {
	        page = maintenanceDAO.findByEquipmentId(equipmentId, pg);
 	    }else if (maintenanceType != null && !maintenanceType.isBlank()) {
	        page = maintenanceDAO.findByMaintenanceType(maintenanceType, pg);
 	    }else if (priorityLevel != null && !priorityLevel.isBlank()) {
	        page = maintenanceDAO.findByPriorityLevel(priorityLevel, pg);
 	    }else if (status != null && !status.isBlank()) {
	        page = maintenanceDAO.findByStatus(status, pg);
 	    }else {
	        page = maintenanceDAO.findAllWorkOrder(pg);
 	    }

	    return page;
	}

	@Override
	public Long saveBreakDown(BreakDown breakDown) {
		if(breakDown.getPriorityLevel().equalsIgnoreCase("CRITICAL")) 
			messagePublisher.MessageCriticalBreakdown(breakDown.getEquipmentId(), breakDown.getFailureReport()	,breakDown.getBreakDownId());
 		return maintenanceDAO.saveBreakDown(breakDown);
	}

	@Override
	public BreakDown getBreakDownbyId(Long breakDownId) throws RecordNotFoundException {
 		return maintenanceDAO.getBreakDownbyId(breakDownId);
	}

	@Override
	public int updateBreakDownStatus(String status, Long breakDownId) throws RecordNotFoundException {
 		return maintenanceDAO.updateBreakDownStatus(status, breakDownId);
	}

	@Override
	public List<BreakDown> findBreakdownsByStatus(String status) {
 		return maintenanceDAO.findByStatus(status);
	}

	@Override
	public MaintenanceTeam getMaintenanceTeamBySupervisor(String Supervisor) throws RecordNotFoundException {
 		return maintenanceDAO.getMaintenanceTeamBySupervisor(Supervisor);
	}

	@Override
	public List<MaintenanceTeam> getAllMaintenanceTeamsList() {
 		return maintenanceDAO.getAllMaintenanceTeamsList();
	}

	@Override
	public List<String> getMaintenanceTeamSupervisorList() {
 		return maintenanceDAO.getMaintenanceTeamSupervisorList();
	}

	@Override
	public int addMainteneceTeamTeammate(String supervisor, String teammate) throws RecordNotFoundException {
 		return maintenanceDAO.addMainteneceTeamTeammate(supervisor, teammate);
	}

	@Override
	public int deleteMainteneceTeamTeammate(String supervisor, String teammate) throws RecordNotFoundException {
 		return maintenanceDAO.deleteMainteneceTeamTeammate(supervisor, teammate);
	}

	@Override
	public int addMainteneceTeamWorkOrderId(String supervisor, Long workOrderID) throws RecordNotFoundException {
 		return maintenanceDAO.addMainteneceTeamWorkOrderId(supervisor, workOrderID);
	}

	@Override
	public int deleteMainteneceTeamWorkOrderId(String supervisor, Long workOrderID) throws RecordNotFoundException {
 		return maintenanceDAO.deleteMainteneceTeamWorkOrderId(supervisor, workOrderID);
	}

	@Override
	public String addMaintenanceTeam(MaintenanceTeam maintenanceTeam) {
 		return maintenanceDAO.addMaintenanceTeam(maintenanceTeam);
	}

	@Override
	public String EmployeeRegisteredInAnyTeam(String teammate) {
		 
		List<String> supervisorList=maintenanceDAO.getMaintenanceTeamSupervisorList();
		for(String supervisor: supervisorList) {
			List<String> teamMatesList=maintenanceDAO.getMaintenanceTeamTeammatesList(supervisor);
			for(String worker:teamMatesList) {
				if(teammate.equalsIgnoreCase(worker))
					return supervisor;
			}
		}
 		return null;
	}

	@Override
	public List<Long> getWorkOrderListDoneByMaintenaceTeam(String supervisor) {
 		return maintenanceDAO.getWorkOrderListDoneByMaintenaceTeam(supervisor);
	}

	@Override
	public String getWorkOrderMaintenanceTeamSupervisor(Long workOrderID) {
 		return maintenanceDAO.getWorkOrderMaintenanceTeamSupervisor(workOrderID);
	}

	@Override
	public int removeWorkOrderMaintenanceTeamSupervisor(Long WorkOrderID) {
 		return maintenanceDAO.removeWorkOrderMaintenanceTeamSupervisor(WorkOrderID);
	}

	@Override
	public int updateWorkOrderMaintenanceTeamSupervisor(String maintenanceTeam, Long workOrderID) {
 		return maintenanceDAO.updateWorkOrderMaintenanceTeamSupervisor(maintenanceTeam, workOrderID);
	}

	@Override
	public String getMaintenanceStatusbyId(Long workOrderID) {
 		return maintenanceDAO.getMaintenanceStatusbyId(workOrderID);
	}

	@Override
	public Page<New_Product_Req> getNew_Product_ReqList(Pageable pg) {
 		return maintenanceDAO.getNew_Product_ReqList(pg);
	}

	@Override
	public Long addNew_Product_Req(New_Product_Req new_Product_Req) {
 		return maintenanceDAO.addNew_Product_Req(new_Product_Req);
	}

	@Override
	public Page<New_Product_Req> getNew_Product_ReqByWorkOrder(Long workOrderId, Pageable pg) {
 		return maintenanceDAO.getNew_Product_ReqByWorkOrder(workOrderId, pg);
	}

	@Override
	public String getMaintenanceType(Long workOrderId) {
 		return maintenanceDAO.getMaintenanceType(workOrderId);
	}

	@Override
	public List<String> getMaintenanceTeamTeammatesList(String supervisor) {
 		return maintenanceDAO.getMaintenanceTeamTeammatesList(supervisor);
	}

	@Override
	public void deleteMaintenanceTeam(String supervisor) {
		maintenanceDAO.deleteMaintenanceTeam(supervisor);
	}

	@Override
	public void deleteEmployeeFromTeams(String email) {
		List<String> spvsrList=maintenanceDAO.getMaintenanceTeamSupervisorList();
		for(String sprvsr:spvsrList) {
			if(sprvsr.equalsIgnoreCase(email)) {
				maintenanceDAO.deleteMaintenanceTeam(email);
				return ;
			}
		}
		 for(String team:spvsrList) {
			 try {
				 List<String> teammateList=maintenanceDAO.getMaintenanceTeamTeammatesList(team);
				 for(String emp:teammateList) {
					 if(emp.equalsIgnoreCase(email)){
						 maintenanceDAO.deleteMainteneceTeamTeammate(team, email);
 							 break;
						}
				 }
			 } catch (RecordNotFoundException e) {
					e.printStackTrace();
			}
		 }
		return ;
	}

	
}
