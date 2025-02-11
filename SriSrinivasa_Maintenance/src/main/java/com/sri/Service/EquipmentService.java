package com.sri.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.sri.Entity.Equipment;
import com.sri.Entity.Location;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Exception.RecordAlreadyExistException;
import com.sri.Exception.RecordNotFoundException;

public interface EquipmentService {

	String addEquipment(Equipment equipment);
	String addAllEquipment(List<Equipment> equipments);
	Equipment getEquipmentbyId(Long equipmentId) throws RecordNotFoundException;
	List<String> getManufacturersList();
	
	String addLocation(Location location) throws RecordAlreadyExistException;
	List<Location> getLocationsList();
	Location getLocationbyId(String locationName) throws RecordNotFoundException;
	List<String> getAllLocationsName();
	
	Page<Equipment> getEquipmentList(String equipmentId,String manufacturer,String department,String category,
	String location,String criticalityLevel,String operationalStatus,String maintenanceFrequency,Pageable pg);

	//Maintenance
	List<MaterialRequiredEquipment> getMaintenanceMaterialsbyId(Long equipmentId) throws RecordNotFoundException;
  	 int updateMaintenanceMaterialsbyId(List<MaterialRequiredEquipment> maintenanceMaterials,Long equipmentId) throws RecordNotFoundException;
     String getMaintenanceFrequencyById(Long equipmentId) throws RecordNotFoundException;

     int removeMaintenaceMaterialfromEquipment(Long eqpmntId,Long PrdctId) throws RecordNotFoundException;
     int addManitenanceMaterialToEquipmwnt(Long EqpmntId,MaterialRequiredEquipment eqpmntMaterial) throws RecordNotFoundException;
     int changeEqpmntMatrlQuanity(Long eqpmntId,Long PrdctId,Integer qty) throws RecordNotFoundException;
	List<Equipment> parseEquipmentCsv(MultipartFile file) throws IOException;
}
