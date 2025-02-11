package com.sri.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sri.Entity.Equipment;
import com.sri.Entity.Location;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Exception.RecordAlreadyExistException;
import com.sri.Exception.RecordNotFoundException;
import com.sri.Persistance.EquipmentDAO;

@Service
public class EquipmentServiceImpl implements EquipmentService{

	@Autowired
	EquipmentDAO dao;
	
	@Override
	public String addEquipment(Equipment equipment) {
		equipment.setNextMaintenanceDate(getNextMaintenanceDate(equipment.getMaintenanceFrequency()));
 		return dao.addEquipment(equipment);
	}

	public static LocalDate getNextMaintenanceDate(String frequency) {
		LocalDate currentDate=LocalDate.now();
		switch(frequency){
			case "Daily":return currentDate.plusDays(1); 
			case "Weekly":return currentDate.plusWeeks(1);
			case "Monthly":return currentDate.plusMonths(1);
			case "Quarterly":return currentDate.plusMonths(3);
			case "Biannually":return currentDate.plusMonths(6);
			case "Annually":return currentDate.plusYears(1);		
		}
		return currentDate.plusMonths(1);
	}
	
	@Override
	public Equipment getEquipmentbyId(Long equipmentId) throws RecordNotFoundException {
 		return dao.getEquipmentbyId(equipmentId);
	}

	@Override
	public String addLocation(Location location) throws RecordAlreadyExistException {
 		return dao.addLocation(location);
	}

	@Override
	public Location getLocationbyId(String locationName) throws RecordNotFoundException {
 		return dao.getLocationbyId(locationName);
	}

	@Override
	public List<String> getAllLocationsName() {
		return dao.getAllLocationsName();
	}

	@Override
	public Page<Equipment> getEquipmentList(String equipmentId,String manufacturer,String department,String category,
			String location,String criticalityLevel,String operationalStatus,String maintenanceFrequency,Pageable pg) {
	    Page<Equipment> page = null;
	    String filter = "Equipments of : ";

	    // Equipment ID Filter
	    if (!equipmentId.isBlank()) {
	        try {
	            Long id = Long.parseLong(equipmentId);
	            page = dao.findByEquipmentId(id, pg);
	            filter = filter + "ID = " + id;
	        } catch (Exception e) {
	            page = dao.findByEquipmentId(0L, pg);
	            filter = filter + "ID = " + equipmentId;
	        }
	    } 
	    // Manufacturer Filter
	    else if (!manufacturer.isBlank()) {
	        page = dao.findByManufacturer(manufacturer, pg);
	        filter = filter + "Manufacturer: " + manufacturer;
	    } 
	    // Department Filter
	    else if (!department.isBlank() && category.isBlank() && location.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartment(department, pg);
	        filter = filter + "Department: " + department;
	    } 
	    // Category Filter
	    else if (!category.isBlank() && department.isBlank() && location.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategory(category, pg);
	        filter = filter + "Category: " + category;
	    } 
	    // Location Filter
	    else if (!location.isBlank() && department.isBlank() && category.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByLocation(location, pg);
	        filter = filter + "Location: " + location;
	    } 
	    // Criticality Level Filter
	    else if (!criticalityLevel.isBlank() && department.isBlank() && category.isBlank() && location.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCriticalityLevel(criticalityLevel, pg);
	        filter = filter + "Criticality Level: " + criticalityLevel;
	    } 
	    // Operational Status Filter
	    else if (!operationalStatus.isBlank() && department.isBlank() && category.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByOperationalStatus(operationalStatus, pg);
	        filter = filter + "Operational Status: " + operationalStatus;
	    } 
	    // Maintenance Frequency Filter
	    else if (!maintenanceFrequency.isBlank() && department.isBlank() && category.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByMaintenanceFrequency(maintenanceFrequency, pg);
	        filter = filter + "Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Department and Category Filter
	    else if (!department.isBlank() && !category.isBlank() && location.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCategory(department, category, pg);
	        filter = filter + "Department: " + department + ", Category: " + category;
	    } 
	    // Department and Criticality Level Filter
	    else if (!department.isBlank() && !criticalityLevel.isBlank() && location.isBlank() && category.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCriticalityLevel(department, criticalityLevel, pg);
	        filter = filter + "Department: " + department + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Department and Location Filter
	    else if (!department.isBlank() && !location.isBlank() && category.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndLocation(department, location, pg);
	        filter = filter + "Department: " + department + ", Location: " + location;
	    } 
	    // Department and Operational Status Filter
	    else if (!department.isBlank() && !operationalStatus.isBlank() && category.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndOperationalStatus(department, operationalStatus, pg);
	        filter = filter + "Department: " + department + ", Operational Status: " + operationalStatus;
	    } 
	    // Department and Maintenance Frequency Filter
	    else if (!department.isBlank() && !maintenanceFrequency.isBlank() && category.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByDepartmentAndMaintenanceFrequency(department, maintenanceFrequency, pg);
	        filter = filter + "Department: " + department + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Category and Location Filter
	    else if (!category.isBlank() && !location.isBlank() && department.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategoryAndLocation(category, location, pg);
	        filter = filter + "Category: " + category + ", Location: " + location;
	    } 
	    // Category and Criticality Level Filter
	    else if (!category.isBlank() && !criticalityLevel.isBlank() && department.isBlank() && location.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategoryAndCriticalityLevel(category, criticalityLevel, pg);
	        filter = filter + "Category: " + category + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Category and Operational Status Filter
	    else if (!category.isBlank() && !operationalStatus.isBlank() && department.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategoryAndOperationalStatus(category, operationalStatus, pg);
	        filter = filter + "Category: " + category + ", Operational Status: " + operationalStatus;
	    } 
	    // Category and Maintenance Frequency Filter
	    else if (!category.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByCategoryAndMaintenanceFrequency(category, maintenanceFrequency, pg);
	        filter = filter + "Category: " + category + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Location and Criticality Level Filter
	    else if (!location.isBlank() && !criticalityLevel.isBlank() && department.isBlank() && category.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByLocationAndCriticalityLevel(location, criticalityLevel, pg);
	        filter = filter + "Location: " + location + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Location and Operational Status Filter
	    else if (!location.isBlank() && !operationalStatus.isBlank() && department.isBlank() && category.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByLocationAndOperationalStatus(location, operationalStatus, pg);
	        filter = filter + "Location: " + location + ", Operational Status: " + operationalStatus;
	    } 
	    // Location and Maintenance Frequency Filter
	    else if (!location.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() && category.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByLocationAndMaintenanceFrequency(location, maintenanceFrequency, pg);
	        filter = filter + "Location: " + location + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Criticality Level and Operational Status Filter
	    else if (!criticalityLevel.isBlank() && !operationalStatus.isBlank() && department.isBlank() && category.isBlank() 
	             && location.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCriticalityLevelAndOperationalStatus(criticalityLevel, operationalStatus, pg);
	        filter = filter + "Criticality Level: " + criticalityLevel + ", Operational Status: " + operationalStatus;
	    } 
	    // Criticality Level and Maintenance Frequency Filter
	    else if (!criticalityLevel.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() && category.isBlank() 
	             && location.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByCriticalityLevelAndMaintenanceFrequency(criticalityLevel, maintenanceFrequency, pg);
	        filter = filter + "Criticality Level: " + criticalityLevel + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Operational Status and Maintenance Frequency Filter
	    else if (!operationalStatus.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() && category.isBlank() 
	             && location.isBlank() && criticalityLevel.isBlank()) {
	        page = dao.findByOperationalStatusAndMaintenanceFrequency(operationalStatus, maintenanceFrequency, pg);
	        filter = filter + "Operational Status: " + operationalStatus + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Department, Category, and Location Filter
	    else if (!department.isBlank() && !category.isBlank() && !location.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCategoryAndLocation(department, category, location, pg);
	        filter = filter + "Department: " + department + ", Category: " + category + ", Location: " + location;
	    }
	    // Department, Category, and Criticality Level Filter
	    else if (!department.isBlank() && !category.isBlank() && !criticalityLevel.isBlank() && location.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCategoryAndCriticalityLevel(department, category, criticalityLevel, pg);
	        filter = filter + "Department: " + department + ", Category: " + category + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Department, Category, and Location Filter
	    else if (!department.isBlank() && !category.isBlank() && !location.isBlank() && criticalityLevel.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCategoryAndLocation(department, category, location, pg);
	        filter = filter + "Department: " + department + ", Category: " + category + ", Location: " + location;
	    } 
	    // Department, Category, and Criticality Level Filter
	    else if (!department.isBlank() && !category.isBlank() && !criticalityLevel.isBlank() && location.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCategoryAndCriticalityLevel(department, category, criticalityLevel, pg);
	        filter = filter + "Department: " + department + ", Category: " + category + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Department, Category, and Operational Status Filter
	    else if (!department.isBlank() && !category.isBlank() && !operationalStatus.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCategoryAndOperationalStatus(department, category, operationalStatus, pg);
	        filter = filter + "Department: " + department + ", Category: " + category + ", Operational Status: " + operationalStatus;
	    } 
	    // Department, Category, and Maintenance Frequency Filter
	    else if (!department.isBlank() && !category.isBlank() && !maintenanceFrequency.isBlank() && location.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByDepartmentAndCategoryAndMaintenanceFrequency(department, category, maintenanceFrequency, pg);
	        filter = filter + "Department: " + department + ", Category: " + category + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Department, Location, and Criticality Level Filter
	    else if (!department.isBlank() && !location.isBlank() && !criticalityLevel.isBlank() && category.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndLocationAndCriticalityLevel(department, location, criticalityLevel, pg);
	        filter = filter + "Department: " + department + ", Location: " + location + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Department, Location, and Operational Status Filter
	    else if (!department.isBlank() && !location.isBlank() && !operationalStatus.isBlank() && category.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndLocationAndOperationalStatus(department, location, operationalStatus, pg);
	        filter = filter + "Department: " + department + ", Location: " + location + ", Operational Status: " + operationalStatus;
	    } 
	    // Department, Location, and Maintenance Frequency Filter
	    else if (!department.isBlank() && !location.isBlank() && !maintenanceFrequency.isBlank() && category.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByDepartmentAndLocationAndMaintenanceFrequency(department, location, maintenanceFrequency, pg);
	        filter = filter + "Department: " + department + ", Location: " + location + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Department, Criticality Level, and Operational Status Filter
	    else if (!department.isBlank() && !criticalityLevel.isBlank() && !operationalStatus.isBlank() && category.isBlank() 
	             && location.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByDepartmentAndCriticalityLevelAndOperationalStatus(department, criticalityLevel, operationalStatus, pg);
	        filter = filter + "Department: " + department + ", Criticality Level: " + criticalityLevel + ", Operational Status: " + operationalStatus;
	    } 
	    // Department, Criticality Level, and Maintenance Frequency Filter
	    else if (!department.isBlank() && !criticalityLevel.isBlank() && !maintenanceFrequency.isBlank() && category.isBlank() 
	             && location.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByDepartmentAndCriticalityLevelAndMaintenanceFrequency(department, criticalityLevel, maintenanceFrequency, pg);
	        filter = filter + "Department: " + department + ", Criticality Level: " + criticalityLevel + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Department, Operational Status, and Maintenance Frequency Filter
	    else if (!department.isBlank() && !operationalStatus.isBlank() && !maintenanceFrequency.isBlank() && category.isBlank() 
	             && location.isBlank() && criticalityLevel.isBlank()) {
	        page = dao.findByDepartmentAndOperationalStatusAndMaintenanceFrequency(department, operationalStatus, maintenanceFrequency, pg);
	        filter = filter + "Department: " + department + ", Operational Status: " + operationalStatus + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Category, Location, and Criticality Level Filter
	    else if (!category.isBlank() && !location.isBlank() && !criticalityLevel.isBlank() && department.isBlank() 
	             && operationalStatus.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategoryAndLocationAndCriticalityLevel(category, location, criticalityLevel, pg);
	        filter = filter + "Category: " + category + ", Location: " + location + ", Criticality Level: " + criticalityLevel;
	    } 
	    // Category, Location, and Operational Status Filter
	    else if (!category.isBlank() && !location.isBlank() && !operationalStatus.isBlank() && department.isBlank() 
	             && criticalityLevel.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategoryAndLocationAndOperationalStatus(category, location, operationalStatus, pg);
	        filter = filter + "Category: " + category + ", Location: " + location + ", Operational Status: " + operationalStatus;
	    } 
	    // Category, Location, and Maintenance Frequency Filter
	    else if (!category.isBlank() && !location.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() 
	             && criticalityLevel.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByCategoryAndLocationAndMaintenanceFrequency(category, location, maintenanceFrequency, pg);
	        filter = filter + "Category: " + category + ", Location: " + location + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Category, Criticality Level, and Operational Status Filter
	    else if (!category.isBlank() && !criticalityLevel.isBlank() && !operationalStatus.isBlank() && department.isBlank() 
	             && location.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByCategoryAndCriticalityLevelAndOperationalStatus(category, criticalityLevel, operationalStatus, pg);
	        filter = filter + "Category: " + category + ", Criticality Level: " + criticalityLevel + ", Operational Status: " + operationalStatus;
	    } 
	    // Category, Criticality Level, and Maintenance Frequency Filter
	    else if (!category.isBlank() && !criticalityLevel.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() 
	             && location.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByCategoryAndCriticalityLevelAndMaintenanceFrequency(category, criticalityLevel, maintenanceFrequency, pg);
	        filter = filter + "Category: " + category + ", Criticality Level: " + criticalityLevel + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Category, Operational Status, and Maintenance Frequency Filter
	    else if (!category.isBlank() && !operationalStatus.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() 
	             && location.isBlank() && criticalityLevel.isBlank()) {
	        page = dao.findByCategoryAndOperationalStatusAndMaintenanceFrequency(category, operationalStatus, maintenanceFrequency, pg);
	        filter = filter + "Category: " + category + ", Operational Status: " + operationalStatus + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Location, Criticality Level, and Operational Status Filter
	    else if (!location.isBlank() && !criticalityLevel.isBlank() && !operationalStatus.isBlank() && department.isBlank() 
	             && category.isBlank() && maintenanceFrequency.isBlank()) {
	        page = dao.findByLocationAndCriticalityLevelAndOperationalStatus(location, criticalityLevel, operationalStatus, pg);
	        filter = filter + "Location: " + location + ", Criticality Level: " + criticalityLevel + ", Operational Status: " + operationalStatus;
	    } 
	    // Location, Criticality Level, and Maintenance Frequency Filter
	    else if (!location.isBlank() && !criticalityLevel.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() 
	             && category.isBlank() && operationalStatus.isBlank()) {
	        page = dao.findByLocationAndCriticalityLevelAndMaintenanceFrequency(location, criticalityLevel, maintenanceFrequency, pg);
	        filter = filter + "Location: " + location + ", Criticality Level: " + criticalityLevel + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Location, Operational Status, and Maintenance Frequency Filter
	    else if (!location.isBlank() && !operationalStatus.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() 
	             && category.isBlank() && criticalityLevel.isBlank()) {
	        page = dao.findByLocationAndOperationalStatusAndMaintenanceFrequency(location, operationalStatus, maintenanceFrequency, pg);
	        filter = filter + "Location: " + location + ", Operational Status: " + operationalStatus + ", Maintenance Frequency: " + maintenanceFrequency;
	    } 
	    // Criticality Level, Operational Status, and Maintenance Frequency Filter
	    else if (!criticalityLevel.isBlank() && !operationalStatus.isBlank() && !maintenanceFrequency.isBlank() && department.isBlank() 
	             && category.isBlank() && location.isBlank()) {
	        page = dao.findByCriticalityLevelAndOperationalStatusAndMaintenanceFrequency(criticalityLevel, operationalStatus, maintenanceFrequency, pg);
	        filter = filter + "Criticality Level: " + criticalityLevel + ", Operational Status: " + operationalStatus + ", Maintenance Frequency: " + maintenanceFrequency;
	    }  else {
	        page = dao.findAllEquipmetList(pg);
	        filter = "All Equipments";
	    }

	    Map<String,Object> map = new HashMap<>();
	    map.put("Page", page);
	    map.put("filter", filter);
	    return page;
	}

	
	//Maintenance
	@Override
	public List<String> getManufacturersList() {
		return dao.getManufacturersList();
	}

	@Override
	public 	List<MaterialRequiredEquipment> getMaintenanceMaterialsbyId(Long equipmentId) throws RecordNotFoundException {
 		return dao.getMaintenanceMaterialsbyId(equipmentId);
	}

	@Override
	public int updateMaintenanceMaterialsbyId(	List<MaterialRequiredEquipment> NewMaintenanceMaterials, Long equipmentId)
			throws RecordNotFoundException {
		List<MaterialRequiredEquipment> oldMaterialList=dao.getMaintenanceMaterialsbyId(equipmentId);
        if(oldMaterialList!=null&&!oldMaterialList.isEmpty()) {
        	oldMaterialList.forEach(mat->{
         		if(!NewMaintenanceMaterials.contains(mat)) {
        			NewMaintenanceMaterials.add(mat);
        		}else {
        			mat.setEquipment(null);
        		}
        	}); 
        } 
 		return dao.updateMaintenanceMaterialsbyId(NewMaintenanceMaterials, equipmentId);
	}

	@Override
	public String getMaintenanceFrequencyById(Long equipmentId) throws RecordNotFoundException {
 		return dao.getMaintenanceFrequencyById(equipmentId);
	}

	@Override
	public int removeMaintenaceMaterialfromEquipment(Long eqpmntId, Long PrdctId) throws RecordNotFoundException {
		List<MaterialRequiredEquipment> oldMaterialList=dao.getMaintenanceMaterialsbyId(eqpmntId);
		System.out.println("\n\n"+oldMaterialList);

		oldMaterialList.forEach(mat->{
			System.out.println(Long.compare(mat.getProductId(),PrdctId)==0?true:false);
			 if(Long.compare(mat.getProductId(),PrdctId)==0?true:false)
				 mat.setEquipment(null);
		});
		System.out.println("\n\n"+oldMaterialList);

 		return dao.updateMaintenanceMaterialsbyId(oldMaterialList, eqpmntId);
	}

	@Override
	public int addManitenanceMaterialToEquipmwnt(Long EqpmntId, MaterialRequiredEquipment eqpmntMaterial) throws RecordNotFoundException {
		List<MaterialRequiredEquipment> oldMaterialList=dao.getMaintenanceMaterialsbyId(EqpmntId);
		oldMaterialList.add(eqpmntMaterial);
 		return dao.updateMaintenanceMaterialsbyId(oldMaterialList, EqpmntId);
	}

	@Override
	public int changeEqpmntMatrlQuanity(Long eqpmntId, Long PrdctId, Integer qty) throws RecordNotFoundException {
		List<MaterialRequiredEquipment> oldMaterialList=dao.getMaintenanceMaterialsbyId(eqpmntId);
		System.out.println("\n\n"+oldMaterialList);
		oldMaterialList.forEach(mat->{
			System.out.println(Long.compare(mat.getProductId(),PrdctId)==0?true:false);
			 if(Long.compare(mat.getProductId(),PrdctId)==0?true:false)
				 mat.setQuantityRequired(qty);
		});
		System.out.println("\n\n"+oldMaterialList);

 		return dao.updateMaintenanceMaterialsbyId(oldMaterialList, eqpmntId);
	}

	@Override
	public List<Location> getLocationsList() {
 		return dao.getLocationsList();
	}

	@Override
	public List<Equipment> parseEquipmentCsv(MultipartFile file) throws IOException {
	    List<Equipment> equipmentList = new ArrayList<>();

	    InputStreamReader reader = new InputStreamReader(file.getInputStream());
	    BufferedReader bufferedReader = new BufferedReader(reader);
	    String line;

	    // Skip header row
	    bufferedReader.readLine();

	    // Regex pattern to handle quoted fields with commas
	    Pattern pattern = Pattern.compile("\"([^\"]*)\"|([^\",]+)");

	    while ((line = bufferedReader.readLine()) != null) {
	        // Split line using regex pattern to handle commas inside quotes
	        Matcher matcher = pattern.matcher(line);
	        List<String> columns = new ArrayList<>();

	        while (matcher.find()) {
	            if (matcher.group(1) != null) {
	                columns.add(matcher.group(1)); // Quoted field
	            } else if (matcher.group(2) != null) {
	                columns.add(matcher.group(2)); // Non-quoted field
	            }
	        }
	        if (columns.size() < 14) {
	            continue; // Skip invalid rows (fewer than 14 columns)
	        }

	        // Create an Equipment object
	        Equipment equipment = new Equipment();
	        equipment.setName(columns.get(0));
	        equipment.setManufacturer(columns.get(1));
	        equipment.setModelNumber(columns.get(2));
	        equipment.setDepartment(columns.get(3));
	        equipment.setCategory(columns.get(4));
	        equipment.setWarrantyInformation(columns.get(5));
	        equipment.setCriticalityLevel(columns.get(6));
	        equipment.setOperationalStatus(columns.get(7));
	        equipment.setOperatingHours(Integer.parseInt(columns.get(8)));
	        equipment.setCost(Double.parseDouble(columns.get(9)));
	        equipment.setNotes(columns.get(10));
	        equipment.setLocation(columns.get(11));
	        equipment.setMaintenanceFrequency(columns.get(12));
	        equipment.setMaintenanceInformation(columns.get(13));
	        // Define the date formatter for the expected CSV format (dd-MM-yyyy)
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	        // Parse the next maintenance date with the custom formatter
            String dateStr = columns.get(14);
            LocalDate nextMaintenanceDate = LocalDate.parse(dateStr, dateFormatter);
            equipment.setNextMaintenanceDate(nextMaintenanceDate);
	        equipmentList.add(equipment);
	    }

	    bufferedReader.close();
	    return equipmentList;
	}

	@Override
	public String addAllEquipment(List<Equipment> equipments) {
		return dao.addAllEquipment(equipments);
	}

}
