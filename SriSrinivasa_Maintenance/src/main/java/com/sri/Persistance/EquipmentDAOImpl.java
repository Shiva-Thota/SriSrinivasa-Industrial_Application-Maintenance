package com.sri.Persistance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.sri.Entity.Equipment;
import com.sri.Entity.Location;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Exception.RecordAlreadyExistException;
import com.sri.Exception.RecordNotFoundException;
import com.sri.Repository.EquipmentRepository;
import com.sri.Repository.LocationRepository;

@Repository
public class EquipmentDAOImpl implements EquipmentDAO{

	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	EquipmentRepository equipmentRepository;
	
	@Override
	public String addEquipment(Equipment equipment) {
		return equipmentRepository.save(equipment).getEquipmentId()+"";
	}

	@Override
	public Equipment getEquipmentbyId(Long equipmentId) throws RecordNotFoundException {
		if(!equipmentRepository.existsById(equipmentId))
			      throw new RecordNotFoundException();
		return equipmentRepository.findById(equipmentId).get();
	}

	@Override
	public String addLocation(Location location) throws RecordAlreadyExistException{
		if(locationRepository.existsById(location.getLocationName()))
			throw new RecordAlreadyExistException();
		
		return locationRepository.save(location).getLocationName();
	}

	@Override
	public Location getLocationbyId(String locationName) throws RecordNotFoundException {
		if(!locationRepository.existsById(locationName))
				throw new RecordNotFoundException();
		return locationRepository.findById(locationName).get();
	}

	@Override
	public List<String> getAllLocationsName() {
		return locationRepository.getAllLocationNames();
	}
	
	
	@Override
	public Page<Equipment> findByEquipmentId(Long equipmentId, Pageable pg) {
	    return equipmentRepository.findByEquipmentId(equipmentId, pg);
	}

	@Override
	public Page<Equipment> findByManufacturer(String manufacturer, Pageable pg) {
	    return equipmentRepository.findByManufacturer(manufacturer, pg);
	}

	@Override
	public Page<Equipment> findByDepartment(String department, Pageable pg) {
	    return equipmentRepository.findByDepartment(department, pg);
	}

	@Override
	public Page<Equipment> findByCategory(String category, Pageable pg) {
	    return equipmentRepository.findByCategory(category, pg);
	}

	@Override
	public Page<Equipment> findByLocation(String location, Pageable pg) {
	    return equipmentRepository.findByLocation(location, pg);
	}

	@Override
	public Page<Equipment> findByCriticalityLevel(String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByCriticalityLevel(criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByOperationalStatus(String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByOperationalStatus(operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByMaintenanceFrequency(String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByMaintenanceFrequency(maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCategory(String department, String category, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCategory(department, category, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCriticalityLevel(String department, String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCriticalityLevel(department, criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndLocation(String department, String location, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndLocation(department, location, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndOperationalStatus(String department, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndOperationalStatus(department, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndMaintenanceFrequency(String department, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndMaintenanceFrequency(department, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndLocation(String category, String location, Pageable pg) {
	    return equipmentRepository.findByCategoryAndLocation(category, location, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndCriticalityLevel(String category, String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByCategoryAndCriticalityLevel(category, criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndOperationalStatus(String category, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByCategoryAndOperationalStatus(category, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndMaintenanceFrequency(String category, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByCategoryAndMaintenanceFrequency(category, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByLocationAndCriticalityLevel(String location, String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByLocationAndCriticalityLevel(location, criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByLocationAndOperationalStatus(String location, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByLocationAndOperationalStatus(location, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByLocationAndMaintenanceFrequency(String location, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByLocationAndMaintenanceFrequency(location, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByCriticalityLevelAndOperationalStatus(String criticalityLevel, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByCriticalityLevelAndOperationalStatus(criticalityLevel, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByCriticalityLevelAndMaintenanceFrequency(String criticalityLevel, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByCriticalityLevelAndMaintenanceFrequency(criticalityLevel, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByOperationalStatusAndMaintenanceFrequency(String operationalStatus, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByOperationalStatusAndMaintenanceFrequency(operationalStatus, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCategoryAndLocation(String department, String category, String location, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCategoryAndLocation(department, category, location, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCategoryAndCriticalityLevel(String department, String category, String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCategoryAndCriticalityLevel(department, category, criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCategoryAndOperationalStatus(String department, String category, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCategoryAndOperationalStatus(department, category, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCategoryAndMaintenanceFrequency(String department, String category, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCategoryAndMaintenanceFrequency(department, category, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndLocationAndCriticalityLevel(String department, String location, String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndLocationAndCriticalityLevel(department, location, criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndLocationAndOperationalStatus(String department, String location, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndLocationAndOperationalStatus(department, location, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndLocationAndMaintenanceFrequency(String department, String location, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndLocationAndMaintenanceFrequency(department, location, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCriticalityLevelAndOperationalStatus(String department, String criticalityLevel, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCriticalityLevelAndOperationalStatus(department, criticalityLevel, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndCriticalityLevelAndMaintenanceFrequency(String department, String criticalityLevel, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndCriticalityLevelAndMaintenanceFrequency(department, criticalityLevel, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByDepartmentAndOperationalStatusAndMaintenanceFrequency(String department, String operationalStatus, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByDepartmentAndOperationalStatusAndMaintenanceFrequency(department, operationalStatus, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndLocationAndCriticalityLevel(String category, String location, String criticalityLevel, Pageable pg) {
	    return equipmentRepository.findByCategoryAndLocationAndCriticalityLevel(category, location, criticalityLevel, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndLocationAndOperationalStatus(String category, String location, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByCategoryAndLocationAndOperationalStatus(category, location, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndLocationAndMaintenanceFrequency(String category, String location, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByCategoryAndLocationAndMaintenanceFrequency(category, location, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndCriticalityLevelAndOperationalStatus(String category, String criticalityLevel, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByCategoryAndCriticalityLevelAndOperationalStatus(category, criticalityLevel, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndCriticalityLevelAndMaintenanceFrequency(String category, String criticalityLevel, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByCategoryAndCriticalityLevelAndMaintenanceFrequency(category, criticalityLevel, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByCategoryAndOperationalStatusAndMaintenanceFrequency(String category, String operationalStatus, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByCategoryAndOperationalStatusAndMaintenanceFrequency(category, operationalStatus, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByLocationAndCriticalityLevelAndOperationalStatus(String location, String criticalityLevel, String operationalStatus, Pageable pg) {
	    return equipmentRepository.findByLocationAndCriticalityLevelAndOperationalStatus(location, criticalityLevel, operationalStatus, pg);
	}

	@Override
	public Page<Equipment> findByLocationAndCriticalityLevelAndMaintenanceFrequency(String location, String criticalityLevel, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByLocationAndCriticalityLevelAndMaintenanceFrequency(location, criticalityLevel, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByLocationAndOperationalStatusAndMaintenanceFrequency(String location, String operationalStatus, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByLocationAndOperationalStatusAndMaintenanceFrequency(location, operationalStatus, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findByCriticalityLevelAndOperationalStatusAndMaintenanceFrequency(String criticalityLevel, String operationalStatus, String maintenanceFrequency, Pageable pg) {
	    return equipmentRepository.findByCriticalityLevelAndOperationalStatusAndMaintenanceFrequency(criticalityLevel, operationalStatus, maintenanceFrequency, pg);
	}

	@Override
	public Page<Equipment> findAllEquipmetList(Pageable pg) {
		return equipmentRepository.findAll(pg);
	}

	@Override
	public List<String> getManufacturersList() {
		return equipmentRepository.getManufacturersList();
	}

	
	//Maintenance
	@Override
	public List<Equipment> getTodaysMaintenanceEquipmentList() {
		
		return equipmentRepository.getTodaysMaintenanceEquipmentList();
	}

	@Override
	public List<Equipment> findEquipmentForNextTwoDays(LocalDate startDate, LocalDate endDate) {
		return equipmentRepository.findEquipmentForNextTwoDays(startDate, endDate);
	}

	@Override
	public 	List<MaterialRequiredEquipment> getMaintenanceMaterialsbyId(Long equipmentId) throws RecordNotFoundException {
		if(!equipmentRepository.existsById(equipmentId))
								throw new RecordNotFoundException();
		return equipmentRepository.getMaintenanceMaterialsbyId(equipmentId);
	}

	@Override
	public int updateMaintenanceMaterialsbyId(	List<MaterialRequiredEquipment> NewMaintenanceMaterials, Long equipmentId)throws RecordNotFoundException {
		if(!equipmentRepository.existsById(equipmentId))
						throw new RecordNotFoundException();
		
		Equipment eqpmnt=equipmentRepository.findById(equipmentId).get();
        eqpmnt.setMaterialRequiredEquipment(NewMaintenanceMaterials);
		equipmentRepository.save(eqpmnt);
		return 1;
	}

	@Override
	public int updateEquipmentnextMaintenanceDatebyId(LocalDate nextMaintenanceDate, Long equipmentId)
			throws RecordNotFoundException {
		if(!equipmentRepository.existsById(equipmentId))
			throw new RecordNotFoundException();
		
		return equipmentRepository.updateEquipmentnextMaintenanceDatebyId(nextMaintenanceDate,equipmentId);
	}

	@Override
	public String getMaintenanceFrequencyById(Long equipmentId) throws RecordNotFoundException {
		if(!equipmentRepository.existsById(equipmentId))
			throw new RecordNotFoundException();
		return equipmentRepository.getMaintenanceFrequencyById(equipmentId);
	}

	@Override
	public List<Location> getLocationsList() {
 		return locationRepository.findAll();
	}

	@Override
	public String addAllEquipment(List<Equipment> equipments) {
 		 equipmentRepository.saveAll(equipments);
 		return null;
	}

	 

}
