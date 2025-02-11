package com.sri.Persistance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.sri.DTO.New_Product_Req;
import com.sri.Entity.BreakDown;
import com.sri.Entity.MaintenanceTask;
import com.sri.Entity.MaintenanceTeam;
import com.sri.Entity.MaterialRequiredWorkOrder;
import com.sri.Entity.WorkOrder;
import com.sri.Exception.RecordNotFoundException;
import com.sri.Repository.BreakDownRepository;
import com.sri.Repository.MaintenanceTaskRepository;
import com.sri.Repository.MaintenanceTeamRepository;
import com.sri.Repository.NewProductReqRepository;
import com.sri.Repository.WorkOrderRepository;

import jakarta.transaction.Transactional;

@Repository
public class MaintenanceDAOImpl  implements MaintenanceDAO{

	@Autowired
	WorkOrderRepository workOrderRepository;
	
	@Autowired
	MaintenanceTaskRepository maintenanceTaskRepository;
	
	@Autowired
	BreakDownRepository breakDownRepository;
	
	@Autowired
	MaintenanceTeamRepository maintenanceTeamRepository;
	
	@Autowired
	NewProductReqRepository newProductReqRepository;

	@Override
	public WorkOrder getWorkOrderById(Long workOrderId) throws RecordNotFoundException {
		if(!workOrderRepository.existsById(workOrderId))
			throw new RecordNotFoundException();
		return workOrderRepository.findById(workOrderId).get();
	}

	@Override
	public String saveWorkOrder(WorkOrder workOrder) {
		
		return workOrderRepository.save(workOrder).getWorkOrderId()+"";
	}

 	@Override
	public int updateWorkOrderMaterialListbyId(List<MaterialRequiredWorkOrder> materialList, Long workOrderId) throws RecordNotFoundException {
		if(!workOrderRepository.existsById(workOrderId))
			throw new RecordNotFoundException();	    
	    
	        WorkOrder workOrder = workOrderRepository.findById(workOrderId).get();
	        workOrder.setMaterialRequired(materialList);
	        workOrderRepository.save(workOrder);
	        return 1; 
	}

	@Override
	public int updateWorkOrderStatusbyId(String status, Long workOrderId) throws RecordNotFoundException {
		if(!workOrderRepository.existsById(workOrderId))
			throw new RecordNotFoundException();
		return workOrderRepository.updateWorkOrderStatusbyId(status, workOrderId);
	}

	@Override
	public int updateWorkOrderTechnicianFeedbackbyId(String technicianFeedback, Long workOrderId) throws RecordNotFoundException {
		if(!workOrderRepository.existsById(workOrderId))
			throw new RecordNotFoundException();
		return workOrderRepository.updateWorkOrderTechnicianFeedbackbyId(technicianFeedback, workOrderId);
	}

	@Override
	public MaintenanceTask getMaintenanceTaskbyId(Long maintenanceTaskId) throws RecordNotFoundException {
		if(!maintenanceTaskRepository.existsById(maintenanceTaskId))
			 throw new RecordNotFoundException();
 		return maintenanceTaskRepository.findById(maintenanceTaskId).get();
	}

	@Override
	public String saveMaintenanceTask(MaintenanceTask maintenanceTask) {
 		return maintenanceTaskRepository.save(maintenanceTask).getMaintenanceTaskId()+"";
	}

	@Override
	public List<MaintenanceTask> getMaintenanceTaskbyStatus(String status) {
 		return maintenanceTaskRepository.getMaintenanceTaskbyStatus(status);
	}

	@Override
	public List<MaintenanceTask> getAllMaintenanceTasks() {
 		return maintenanceTaskRepository.findAll();
	}

	@Override
	public List<MaterialRequiredWorkOrder> getWorkOrderMaterialRequiredbyId(Long workOrderId) {
		 
		return workOrderRepository.getWorkOrderMaterialRequiredbyId(workOrderId);
	}

	@Override
	public int updateMaintenanceTaskStatusbyId(String status, Long maintenanceTaskId) {
		return maintenanceTaskRepository.updateMaintenanceTaskStatusbyId(status, maintenanceTaskId);
	}

	@Override
	public Page<WorkOrder> findByEquipmentId(Long equipmentId, Pageable pg) {
		return workOrderRepository.findByEquipmentId(equipmentId, pg);
	}

	@Override
	public Page<WorkOrder> findByMaintenanceType(String maintenanceType, Pageable pg) {
		return workOrderRepository.findByMaintenanceType(maintenanceType, pg);
	}


	@Override
	public Page<WorkOrder> findByPriorityLevel(String priorityLevel, Pageable pg) {
 		return workOrderRepository.findByPriorityLevel(priorityLevel, pg);
	}

	@Override
	public Page<WorkOrder> findByWorkOrderId(Long workOrderId, Pageable pg) {
 		return workOrderRepository.findByWorkOrderId(workOrderId, pg);
	}

	@Override
	public Page<WorkOrder> findByStatus(String status, Pageable pg) {
 		return workOrderRepository.findByStatus(status, pg);
	}

	@Override
	public Page<WorkOrder> findAllWorkOrder(Pageable pg) {
		
		return workOrderRepository.findAll(pg);
	}

	@Override
	public Long saveBreakDown(BreakDown breakDown) {
 		return breakDownRepository.save(breakDown).getBreakDownId();
	}

	@Override
	public BreakDown getBreakDownbyId(Long breakDownId) throws RecordNotFoundException {
		if(!breakDownRepository.existsById(breakDownId))
				throw new RecordNotFoundException();
		return breakDownRepository.findById(breakDownId).get();
	}

	@Override
	public int updateBreakDownStatus(String status, Long breakDownId) throws RecordNotFoundException{
		if(!breakDownRepository.existsById(breakDownId))
			throw new RecordNotFoundException();
	 return breakDownRepository.updateBreakDownStatus(status, breakDownId);
	}

	@Override
	public List<BreakDown> findByStatus(String status) {	 
		return breakDownRepository.findByStatus(status);
	}

	@Override
	public MaintenanceTeam getMaintenanceTeamBySupervisor(String Supervisor) throws RecordNotFoundException {
		if(!maintenanceTeamRepository.existsById(Supervisor))
			 throw new RecordNotFoundException();
		return maintenanceTeamRepository.findById(Supervisor).get();
	}

	@Override
	public List<MaintenanceTeam> getAllMaintenanceTeamsList() {
 		return maintenanceTeamRepository.findAll();
	}

	@Override
	public List<String> getMaintenanceTeamSupervisorList() {
 		return maintenanceTeamRepository.getMaintenanceTeamSupervisorList();
	}

	@Override
	public int addMainteneceTeamTeammate(String supervisor, String teammate) throws RecordNotFoundException {
		if(!maintenanceTeamRepository.existsById(supervisor))
			 throw new RecordNotFoundException();
		
	    MaintenanceTeam team=maintenanceTeamRepository.findById(supervisor).get();
		List<String> TeamMatesList=team.getTeamMates();
		TeamMatesList.add(teammate);
		team.setTeamMates(TeamMatesList);
		maintenanceTeamRepository.save(team);
	    
		return 1;
	}

	@Override
	public int deleteMainteneceTeamTeammate(String supervisor, String teammate) throws RecordNotFoundException {
		if(!maintenanceTeamRepository.existsById(supervisor))
			 throw new RecordNotFoundException();
		try {
		 MaintenanceTeam team=maintenanceTeamRepository.findById(supervisor).get();
		 List<String> TeamMatesList=team.getTeamMates();
		 TeamMatesList.remove(teammate);
		 team.setTeamMates(TeamMatesList);
		 maintenanceTeamRepository.save(team);
		}catch(Exception e) {
			return -1;
		}
		return 1;
	}

	@Override
	public int addMainteneceTeamWorkOrderId(String supervisor, Long workOrderID) throws RecordNotFoundException {
		if(!maintenanceTeamRepository.existsById(supervisor))
			 throw new RecordNotFoundException();
		  MaintenanceTeam team=maintenanceTeamRepository.findById(supervisor).get();
			List<Long> workOrderList=team.getWorkOrderIds();
			workOrderList.add(workOrderID);
			team.setWorkOrderIds(workOrderList);
			maintenanceTeamRepository.save(team);
		return 1;
	}

	@Override
	public int deleteMainteneceTeamWorkOrderId(String supervisor, Long workOrderID) throws RecordNotFoundException {
		if(!maintenanceTeamRepository.existsById(supervisor))
			 throw new RecordNotFoundException();
		try {
			MaintenanceTeam team=maintenanceTeamRepository.findById(supervisor).get();
			List<Long> workOrderList=team.getWorkOrderIds();
			workOrderList.remove(workOrderID);
			team.setWorkOrderIds(workOrderList);
			maintenanceTeamRepository.save(team);
		}catch(Exception e) {
			return -1;
		}
		return 1;
	}

	@Override
	public String addMaintenanceTeam(MaintenanceTeam maintenanceTeam) {
 		return maintenanceTeamRepository.save(maintenanceTeam).getSupervisor();
	}

	@Override
	public List<String> getMaintenanceTeamTeammatesList(String supervisor) {
 		return maintenanceTeamRepository.getMainteneceTeamTeammatesListbySupervisor(supervisor);
	}

	@Override
	public List<Long> getWorkOrderListDoneByMaintenaceTeam(String supervisor) {
		
		return workOrderRepository.getWorkOrderListDoneByMaintenaceTeam(supervisor);
	}

	@Override
	public String getWorkOrderMaintenanceTeamSupervisor(Long workOrderID) {
 		return workOrderRepository.getMaintenanceTeamSupervisor(workOrderID);
	}

	@Override
	public int removeWorkOrderMaintenanceTeamSupervisor(Long WorkOrderID) {
 		return workOrderRepository.removeMaintenanceTeamSupervisor(WorkOrderID);
	}

	@Override
	public int updateWorkOrderMaintenanceTeamSupervisor(String maintenanceTeam, Long workOrderID) {
 		return workOrderRepository.updateMaintenanceTeamSupervisor(maintenanceTeam, workOrderID);
	}

	@Override
	public Page<WorkOrder> findByTechnician(String technician, Pageable pg) {
 		return workOrderRepository.findByMaintenanceTeam(technician, pg);
	}

	@Override
	public Page<WorkOrder> findByMaintenanceTeamAndPriorityLevel(String maintenanceTeam, String priorityLevel,
			Pageable pg) {
		return workOrderRepository.findByMaintenanceTeamAndPriorityLevel(maintenanceTeam, priorityLevel, pg);
	}

	@Override
	public Page<WorkOrder> findByMaintenanceTeamAndStatus(String maintenanceTeam, String status, Pageable pg) {
		return workOrderRepository.findByMaintenanceTeamAndStatus(maintenanceTeam, status, pg);
	}

	@Override
	public Page<WorkOrder> findByMaintenanceTeamAndWorkOrderId(String maintenanceTeam, Long workOrderId, Pageable pg) {
		return workOrderRepository.findByMaintenanceTeamAndWorkOrderId(maintenanceTeam, workOrderId, pg);
	}

	@Override
	public Page<WorkOrder> findByMaintenanceTeamAndMaintenanceType(String maintenanceTeam, String maintenanceType,
			Pageable pg) {
		return workOrderRepository.findByMaintenanceTeamAndMaintenanceType(maintenanceTeam, maintenanceType, pg);
	}

	@Override
	public Page<WorkOrder> findByMaintenanceTeamAndEquipmentId(String maintenanceTeam, Long equipmentId, Pageable pg) {
 		return workOrderRepository.findByMaintenanceTeamAndEquipmentId(maintenanceTeam, equipmentId, pg);
	}

	@Override
	public String getMaintenanceStatusbyId(Long workOrderID) {
 		return workOrderRepository.getMaintenanceStatusbyId(workOrderID);
	}

	@Override
	public Page<New_Product_Req> getNew_Product_ReqList(Pageable pg) {
 		return newProductReqRepository.findAll(pg);
	}

	@Override
	public Long addNew_Product_Req(New_Product_Req new_Product_Req) {
 		return newProductReqRepository.save(new_Product_Req).getNewProductReqId();
	}

	@Override
	public Page<New_Product_Req> getNew_Product_ReqByWorkOrder(Long workOrderId,Pageable pg){
 		return newProductReqRepository.findByWorkOrderId(workOrderId, pg);
	}

	@Override
	public String getMaintenanceType(Long workOrderId) {
 		return workOrderRepository.getMaintenanceType(workOrderId);
	}

	@Override
	public void deleteMaintenanceTeam(String supervisor) {
		maintenanceTeamRepository.deleteById(supervisor);
	}
	
	
	
	
}
