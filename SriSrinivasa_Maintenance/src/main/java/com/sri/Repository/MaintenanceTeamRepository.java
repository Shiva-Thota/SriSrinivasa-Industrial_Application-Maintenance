package com.sri.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sri.Entity.MaintenanceTeam;

public interface MaintenanceTeamRepository extends JpaRepository<MaintenanceTeam,String>{

	@Query("Select supervisor from  MaintenanceTeam")
	List<String> getMaintenanceTeamSupervisorList();
	
	@Query("Select teamMates from MaintenanceTeam where supervisor =:supervisor")
	List<String> getMainteneceTeamTeammatesListbySupervisor(String supervisor);
	
	@Query("Select WorkOrderIds from MaintenanceTeam where supervisor =:supervisor")
	List<String> getMainteneceTeamWorkOrdersListbySupervisor(String supervisor);
	
	
}
