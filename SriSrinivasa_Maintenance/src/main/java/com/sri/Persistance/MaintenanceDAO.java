package com.sri.Persistance;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sri.DTO.New_Product_Req;
import com.sri.Entity.BreakDown;
import com.sri.Entity.MaintenanceTask;
import com.sri.Entity.MaintenanceTeam;
import com.sri.Entity.MaterialRequiredWorkOrder;
import com.sri.Entity.WorkOrder;
import com.sri.Exception.RecordNotFoundException;

public interface MaintenanceDAO {
	
	WorkOrder getWorkOrderById(Long workOrderId) throws RecordNotFoundException;
	String saveWorkOrder(WorkOrder workOrder);
	String getMaintenanceStatusbyId(Long workOrderID);
	String getMaintenanceType(Long workOrderId);
  
	int updateWorkOrderMaterialListbyId(List<MaterialRequiredWorkOrder> materialList, Long workOrderId) throws RecordNotFoundException;
	int updateWorkOrderStatusbyId(String status, Long workOrderId) throws RecordNotFoundException;
	int updateWorkOrderTechnicianFeedbackbyId(String technicianFeedback, Long workOrderId) throws RecordNotFoundException;
	List<MaterialRequiredWorkOrder> getWorkOrderMaterialRequiredbyId(Long workOrderId);
	
	Page<WorkOrder> findByEquipmentId(Long equipmentId,Pageable pg);
	Page<WorkOrder> findByMaintenanceType(String maintenanceType,Pageable pg);
 	Page<WorkOrder> findByPriorityLevel(String priorityLevel,Pageable pg);
	Page<WorkOrder> findByWorkOrderId(Long workOrderId,Pageable pg);
	Page<WorkOrder> findByStatus(String status,Pageable pg);
	Page<WorkOrder> findAllWorkOrder(Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndPriorityLevel(String maintenanceTeam, String priorityLevel,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndStatus(String maintenanceTeam, String status,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndWorkOrderId(String maintenanceTeam, Long workOrderId,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndMaintenanceType(String maintenanceTeam, String maintenanceType,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndEquipmentId(String maintenanceTeam, Long equipmentId,Pageable pg);

	
	
	//MaintenanceTask
	MaintenanceTask getMaintenanceTaskbyId(Long maintenanceTaskId) throws RecordNotFoundException;
	String saveMaintenanceTask(MaintenanceTask maintenanceTask);
	List<MaintenanceTask> getMaintenanceTaskbyStatus(String status);
	List<MaintenanceTask> getAllMaintenanceTasks();
	int updateMaintenanceTaskStatusbyId(String status,Long maintenanceTaskId);
	
	//BreakDown
	Long saveBreakDown(BreakDown bteakDown);
	BreakDown getBreakDownbyId(Long breakDownId) throws RecordNotFoundException;
	int updateBreakDownStatus(String status,Long breakDownId) throws RecordNotFoundException;
	List<BreakDown> findByStatus(String status);
	
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
 	int updateWorkOrderMaintenanceTeamSupervisor(String maintenanceTeam,Long workOrderID);
 	List<Long> getWorkOrderListDoneByMaintenaceTeam(String supervisor);
	Page<WorkOrder> findByTechnician(String technician, Pageable pg);
	
	//New Product Request List
	Page<New_Product_Req> getNew_Product_ReqList(Pageable pg);
	Long addNew_Product_Req(New_Product_Req new_Product_Req);
	Page<New_Product_Req> getNew_Product_ReqByWorkOrder(Long workOrderId,Pageable pg);
	void deleteMaintenanceTeam(String supervisor);

 	
}
