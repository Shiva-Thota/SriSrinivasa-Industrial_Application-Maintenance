package com.sri.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sri.DTO.New_Product_Req;
import com.sri.Entity.BreakDown;
import com.sri.Entity.Equipment;
import com.sri.Entity.MaintenanceTask;
import com.sri.Entity.MaintenanceTeam;
import com.sri.Entity.MaterialRequiredWorkOrder;
import com.sri.Entity.WorkOrder;
import com.sri.Exception.RecordNotFoundException;

public interface MaintenanceService {

	//Maintenance
		List<Equipment> getTodaysMaintenanceEquipmentList();
	  	List<Equipment> findEquipmentForSpecifiedDays(LocalDate startDate,LocalDate endDate);
	  	List<Equipment> findEquipmentForNextTwoDays();
	  	
	  	
	 //Work Order
	  	WorkOrder getWorkOrderById(Long workOrderId) throws RecordNotFoundException;
		String saveWorkOrder(WorkOrder workOrder);
		String getMaintenanceStatusbyId(Long workOrderID);
		String getMaintenanceType(Long workOrderId);
	  	
	  	int updateWorkOrderMaterialListbyId(List<MaterialRequiredWorkOrder> materialList, Long workOrderId)throws RecordNotFoundException;
		int updateWorkOrderStatusbyId(String status, Long workOrderId)throws RecordNotFoundException;
		int updateWorkOrderTechnicianFeedbackbyId(String technicianFeedback, Long workOrderId) throws RecordNotFoundException;
		List<MaterialRequiredWorkOrder> getWorkOrderMaterialRequiredbyId(Long workOrderId);
		
		
		//MaintenanceTask
		MaintenanceTask getMaintenanceTaskbyId(Long maintenanceTaskId) throws RecordNotFoundException;
		String saveMaintenanceTask(MaintenanceTask maintenanceTask);
		List<MaintenanceTask> getMaintenanceTaskbyStatus(String status);
		List<MaintenanceTask> getAllMaintenanceTasks();	
		int updateMaintenanceTaskStatusbyId(String status,Long maintenanceTaskId);
		Page<WorkOrder> getWorkOrder(Long equipmentId, Long workOrderId, String maintenanceType, String priorityLevel,String status, String technician, Pageable pageable);
		
		//BreakDown
		Long saveBreakDown(BreakDown bteakDown);
		BreakDown getBreakDownbyId(Long breakDownId) throws RecordNotFoundException;
		int updateBreakDownStatus(String status,Long breakDownId) throws RecordNotFoundException;
		List<BreakDown> findBreakdownsByStatus(String status);
		
		//Maintenance Team
		MaintenanceTeam getMaintenanceTeamBySupervisor(String Supervisor) throws RecordNotFoundException;
		String addMaintenanceTeam(MaintenanceTeam maintenanceTeam);
		List<MaintenanceTeam> getAllMaintenanceTeamsList();
		List<String> getMaintenanceTeamSupervisorList();
		List<String> getMaintenanceTeamTeammatesList(String supervisor);
	 	int addMainteneceTeamTeammate(String supervisor,String teammate)throws RecordNotFoundException;
	 	int deleteMainteneceTeamTeammate(String supervisor,String teammate)throws RecordNotFoundException;
	 	int addMainteneceTeamWorkOrderId(String supervisor,Long workOrderID)throws RecordNotFoundException;
	 	int deleteMainteneceTeamWorkOrderId(String supervisor,Long workOrderID)throws RecordNotFoundException;
	 	String getWorkOrderMaintenanceTeamSupervisor(Long workOrderID);
	 	int removeWorkOrderMaintenanceTeamSupervisor(Long WorkOrderID);
	 	int updateWorkOrderMaintenanceTeamSupervisor(String maintenanceTeam, Long workOrderID) ;
	 	
		String EmployeeRegisteredInAnyTeam(String teammate);
		
		
		List<Long> getWorkOrderListDoneByMaintenaceTeam(String supervisor);

		//New Product Request List
		Page<New_Product_Req> getNew_Product_ReqList(Pageable pg);
		Long addNew_Product_Req(New_Product_Req new_Product_Req);
		Page<New_Product_Req> getNew_Product_ReqByWorkOrder(Long workOrderId,Pageable pg);
		void deleteMaintenanceTeam(String supervisor);
		void deleteEmployeeFromTeams(String email);

}
