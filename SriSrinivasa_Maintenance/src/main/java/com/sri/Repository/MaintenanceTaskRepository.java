package com.sri.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sri.Entity.MaintenanceTask;

import jakarta.transaction.Transactional;

public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {

	@Query("from MaintenanceTask where status =:status")
	List<MaintenanceTask> getMaintenanceTaskbyStatus(String status);
	
	@Transactional
	@Modifying
	@Query("update MaintenanceTask set status =:status where maintenanceTaskId =:maintenanceTaskId")
	int updateMaintenanceTaskStatusbyId(String status,Long maintenanceTaskId);
	
}
