package com.sri.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sri.Entity.MaterialRequiredWorkOrder;
import com.sri.Entity.WorkOrder;

import jakarta.transaction.Transactional;


public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>{

//	@Transactional
//	@Modifying
//	@Query("update WorkOrder set materialRequired =:materialList where workOrderId =:workOrderId")
//	int updateWorkOrderMaterialListbyId(List<MaterialWorkOrder> materialList ,Long workOrderId);
//	
	
	
	
	
	@Transactional
	@Modifying
	@Query("update WorkOrder set status =:status where workOrderId =:workOrderId")
    int updateWorkOrderStatusbyId(String status,Long workOrderId);
	
	@Transactional
	@Modifying
	@Query("update WorkOrder set technicianFeedback =:technicianFeedback where workOrderId =:workOrderId")
	int updateWorkOrderTechnicianFeedbackbyId(String technicianFeedback,Long workOrderId);
	
	@Query("Select maintenanceTeam from WorkOrder where workOrderId =:workOrderID")
	String getMaintenanceTeamSupervisor(Long workOrderID);
	
	@Query("select  status from WorkOrder where workOrderId =:workOrderID")
	String getMaintenanceStatusbyId(Long workOrderID);
	
	@Query("select maintenanceType from WorkOrder where workOrderId =:workOrderId")
	String getMaintenanceType(Long workOrderId);
	
	@Transactional
	@Modifying
	@Query("update WorkOrder set maintenanceTeam = null where workOrderId = :workOrderID")
	int removeMaintenanceTeamSupervisor(Long workOrderID);
	
	@Transactional
	@Modifying
	@Query("update WorkOrder set maintenanceTeam =:maintenanceTeam where workOrderId = :workOrderID")
	int updateMaintenanceTeamSupervisor(String maintenanceTeam,Long workOrderID);
	
	Page<WorkOrder> findByEquipmentId(Long equipmentId,Pageable pg);
	Page<WorkOrder> findByMaintenanceType(String maintenanceType,Pageable pg);
	Page<WorkOrder> findByPriorityLevel(String priorityLevel,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeam(String maintenanceTeam,Pageable pg);
	Page<WorkOrder> findByWorkOrderId(Long workOrderId,Pageable pg);
	Page<WorkOrder> findByStatus(String status,Pageable pg);
	
	Page<WorkOrder> findByMaintenanceTeamAndPriorityLevel(String maintenanceTeam, String priorityLevel,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndStatus(String maintenanceTeam, String status,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndWorkOrderId(String maintenanceTeam, Long workOrderId,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndMaintenanceType(String maintenanceTeam, String maintenanceType,Pageable pg);
	Page<WorkOrder> findByMaintenanceTeamAndEquipmentId(String maintenanceTeam, Long equipmentId,Pageable pg);
	
	@Query("select materialRequired from WorkOrder where workOrderId =:workOrderId")
	List<MaterialRequiredWorkOrder> getWorkOrderMaterialRequiredbyId(Long workOrderId);
	
	@Query("select workOrderId from WorkOrder where maintenanceTeam =:maintenanceTeam")
	List<Long> getWorkOrderListDoneByMaintenaceTeam(String maintenanceTeam);
}
