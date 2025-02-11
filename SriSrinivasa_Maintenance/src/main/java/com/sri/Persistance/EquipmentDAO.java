package com.sri.Persistance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.sri.Entity.Equipment;
import com.sri.Entity.Location;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Exception.RecordAlreadyExistException;
import com.sri.Exception.RecordNotFoundException;

public interface EquipmentDAO {

	String addEquipment(Equipment equipment);
	String addAllEquipment(List<Equipment> equipments);

	Equipment getEquipmentbyId(Long equipmentId) throws RecordNotFoundException;
	
	List<String> getManufacturersList();

	String addLocation(Location location) throws RecordAlreadyExistException;

	Location getLocationbyId(String locationName) throws RecordNotFoundException;
	List<Location> getLocationsList();

	
	 List<String> getAllLocationsName();

	 Page<Equipment> findByEquipmentId(Long equipmentId,Pageable pg);
	 Page<Equipment> findByManufacturer(String manufacturer,Pageable pg);
	 
	 Page<Equipment> findByDepartment(String department,Pageable pg); 
	 Page<Equipment> findByCategory(String category,Pageable pg); 
	 Page<Equipment> findByLocation(String location,Pageable pg);
	 Page<Equipment> findByCriticalityLevel(String criticalityLevel,Pageable pg);
	 Page<Equipment> findByOperationalStatus(String operationalStatus,Pageable pg);
	 Page<Equipment> findByMaintenanceFrequency(String maintenanceFrequency,Pageable pg);
	 
	 Page<Equipment> findByDepartmentAndCategory(String department, String category,Pageable pg); 
	 Page<Equipment> findByDepartmentAndCriticalityLevel(String department, String criticalityLevel,Pageable pg); 
	 Page<Equipment> findByDepartmentAndLocation(String department, String location,Pageable pg); 
	 Page<Equipment> findByDepartmentAndOperationalStatus(String department, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByDepartmentAndMaintenanceFrequency(String department, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByCategoryAndLocation(String category, String location,Pageable pg); 
	 Page<Equipment> findByCategoryAndCriticalityLevel(String category, String criticalityLevel,Pageable pg); 
	 Page<Equipment> findByCategoryAndOperationalStatus(String category, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByCategoryAndMaintenanceFrequency(String category, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByLocationAndCriticalityLevel(String location, String criticalityLevel,Pageable pg); 
	 Page<Equipment> findByLocationAndOperationalStatus(String location, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByLocationAndMaintenanceFrequency(String location, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByCriticalityLevelAndOperationalStatus(String criticalityLevel, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByCriticalityLevelAndMaintenanceFrequency(String criticalityLevel, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByOperationalStatusAndMaintenanceFrequency(String operationalStatus, String maintenanceFrequency,Pageable pg); 	 
	 
	 Page<Equipment> findByDepartmentAndCategoryAndLocation(String department, String category, String location,Pageable pg); 
	 Page<Equipment> findByDepartmentAndCategoryAndCriticalityLevel(String department, String category, String criticalityLevel,Pageable pg); 
	 Page<Equipment> findByDepartmentAndCategoryAndOperationalStatus(String department, String category, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByDepartmentAndCategoryAndMaintenanceFrequency(String department, String category, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByDepartmentAndLocationAndCriticalityLevel(String department, String location, String criticalityLevel,Pageable pg); 
	 Page<Equipment> findByDepartmentAndLocationAndOperationalStatus(String department, String location, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByDepartmentAndLocationAndMaintenanceFrequency(String department, String location, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByDepartmentAndCriticalityLevelAndOperationalStatus(String department, String criticalityLevel, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByDepartmentAndCriticalityLevelAndMaintenanceFrequency(String department, String criticalityLevel, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByDepartmentAndOperationalStatusAndMaintenanceFrequency(String department, String operationalStatus, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByCategoryAndLocationAndCriticalityLevel(String category, String location, String criticalityLevel,Pageable pg); 
	 Page<Equipment> findByCategoryAndLocationAndOperationalStatus(String category, String location, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByCategoryAndLocationAndMaintenanceFrequency(String category, String location, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByCategoryAndCriticalityLevelAndOperationalStatus(String category, String criticalityLevel, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByCategoryAndCriticalityLevelAndMaintenanceFrequency(String category, String criticalityLevel, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByCategoryAndOperationalStatusAndMaintenanceFrequency(String category, String operationalStatus, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByLocationAndCriticalityLevelAndOperationalStatus(String location, String criticalityLevel, String operationalStatus,Pageable pg); 
	 Page<Equipment> findByLocationAndCriticalityLevelAndMaintenanceFrequency(String location, String criticalityLevel, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByLocationAndOperationalStatusAndMaintenanceFrequency(String location, String operationalStatus, String maintenanceFrequency,Pageable pg); 
	 
	 Page<Equipment> findByCriticalityLevelAndOperationalStatusAndMaintenanceFrequency(String criticalityLevel, String operationalStatus, String maintenanceFrequency,Pageable pg);

	 Page<Equipment> findAllEquipmetList(Pageable pg); 

	 //Maintenance
     List<Equipment> getTodaysMaintenanceEquipmentList();
   	 List<Equipment> findEquipmentForNextTwoDays(LocalDate startDate,LocalDate endDate);
     List<MaterialRequiredEquipment> getMaintenanceMaterialsbyId(Long equipmentId) throws RecordNotFoundException;
   	 int updateMaintenanceMaterialsbyId(List<MaterialRequiredEquipment> maintenanceMaterials,Long equipmentId) throws RecordNotFoundException;
   	 int updateEquipmentnextMaintenanceDatebyId(LocalDate nextMaintenanceDate, Long equipmentId) throws RecordNotFoundException;
     String getMaintenanceFrequencyById(Long equipmentId) throws RecordNotFoundException;
     
     
	 
	 
}
