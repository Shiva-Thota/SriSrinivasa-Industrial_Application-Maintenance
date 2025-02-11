package com.sri.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.DTO.Prod_Inv_DTO_Product;
import com.sri.Entity.Equipment;
import com.sri.Entity.Location;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Exception.RecordAlreadyExistException;
import com.sri.Exception.RecordNotFoundException;
import com.sri.FeignClient.Inventory_Client;
import com.sri.Service.EquipmentService;
import com.sri.utils.EquipmetUtils;
import com.sri.validators.EquipmentValidator;

import feign.FeignException;

@Controller
@RequestMapping("/maintenance/eqpmnt")
public class EquipmentController {

	@Autowired
	EquipmentService equipmentService;

	@Autowired
	EquipmentValidator equipmentValidator;
	
	@Autowired
	Inventory_Client inventoryClient;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Value("${gateway.url}")
	String gateWayUrl;
	
	@Value("${modelAttribute.Operational_Status}")
	List<String> operationalStatusList;
	
	@Value("${modelAttribute.Criticality_Level}")
	List<String> criticalityLevelList;
	
	@Value("${modelAttribute.Maintenance_Frequency}")
	List<String> maintenanceFrequencyList;
	
	@Value("${modelAttribute.Department_List}")
	List<String> departmentList;
	
	@Value("${modelAttribute.Category_List}")
	List<String> categoryList;
	
	@GetMapping("/location")
	public String getLocationPage(@ModelAttribute("location") Location location) {
		return "LocationRegister";
	}
	
	@PostMapping("/location")
	public String registerLocation(@ModelAttribute("location") Location location,RedirectAttributes redirectAttributes) {
		try {
			equipmentService.addLocation(location);
			redirectAttributes.addFlashAttribute("Success_Fully", "Successfully Registered");
		}catch(RecordAlreadyExistException e) {
			redirectAttributes.addFlashAttribute("Record_Already_exist", "Record already exists with location"+location.getLocationName());
		}
		return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/location";
	}
	
	@GetMapping("/locationList")
	public String getAllLocationsList(Map<String,Object> map) {
		List<Location> list=equipmentService.getLocationsList();
		map.put("Location_List", list);
		return "LocationList";
	}
	
	
	@GetMapping("/addEquipment")
	public String getAddEquipmentPage(@ModelAttribute("equipment") Equipment equipment) {
		return "EquipmentRegister";
	}
	
	@PostMapping("/addEquipment")
	public String addEquipment(@ModelAttribute("equipment") Equipment equipment,BindingResult error,RedirectAttributes redirectAttributes) {
		if(equipmentValidator.supports(equipment.getClass())) {
			equipmentValidator.validate(equipment, error);
			if(error.hasErrors())
				return "EquipmentRegister";
		}
		try {
			equipmentService.addEquipment(equipment);
			redirectAttributes.addFlashAttribute("Success_Fully", "Successfully Registered");
		}catch(Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("UN_Success", "Internal Problem Occured");
		}
		return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/addEquipment";
	}
	
	@GetMapping("/update/{eqpmntId}")
	public String updateEquipmentPage(@PathVariable("eqpmntId") Long eqpmntId,Map<String,Object> map) {
		try {
			Equipment equipment=equipmentService.getEquipmentbyId(eqpmntId);
			map.put("equipment", equipment);
		} catch (RecordNotFoundException e) {
  		}
		return "EquipmentUpdate";
	}
	
	@PostMapping("/update")
	public String updateEquipment(@ModelAttribute("equipment") Equipment equipment) {
		equipmentService.addEquipment(equipment);
		return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/eqpmntDetails/"+equipment.getEquipmentId();
	}
	
	@GetMapping("/addEqpmntMntncMatPage/{eqpmntId}")
	public String addEquipmentMaintenanceMaterialPage(@PathVariable("eqpmntId") Long eqpmntId,Map<String,Object> map) {
		map.put("EquipmentId", eqpmntId);
		return "EqpmntAddMntnceMat";
	}
	
	@PostMapping("/addEqpmntMntncMat")
	public String addEqpmntMntnceMatrl(@RequestParam("EquipmentId") Long EquipmentId,@RequestParam("ProductId") Long productId,
			@RequestParam("quantity") Integer quantity,RedirectAttributes attributes) {
		try {
			ResponseEntity<?> ProductResponseEntity=  inventoryClient.getProductBasedonID(productId);
			if(ProductResponseEntity.getStatusCode().isError()) {
				return "ErrorInternalServer";
			}
			List<Prod_Inv_DTO_Product> product=(List<Prod_Inv_DTO_Product>) ProductResponseEntity.getBody();
			if(product.isEmpty()) {
				attributes.addFlashAttribute("MaterialNotFound", "Material Not Found in Inventory with ID :"+productId);
				return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/eqpmntDetails/"+EquipmentId;
			}
			Prod_Inv_DTO_Product product_0=objectMapper.convertValue(product.get(0), Prod_Inv_DTO_Product.class);			
			MaterialRequiredEquipment materialRequiredEquipmentfromProd_Inv = EquipmetUtils.getMaterialRequiredEquipmentfromProd_Inv(product_0);
			materialRequiredEquipmentfromProd_Inv.setQuantityRequired(quantity);
			materialRequiredEquipmentfromProd_Inv.setEquipment(equipmentService.getEquipmentbyId(EquipmentId));
			equipmentService.addManitenanceMaterialToEquipmwnt(EquipmentId,materialRequiredEquipmentfromProd_Inv);
		}catch(FeignException e) {
			attributes.addFlashAttribute("MaterialNotFound", "Material Not Found in Inventory with ID :"+productId);
			return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/addEqpmntMntncMatPage/"+EquipmentId;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/eqpmntDetails/"+EquipmentId;
	}
	
	@PostMapping("/removeEqpmntMntnceMat")
	public String removeEqpmntMntnceMat(@RequestParam("eqpmntId") Long EquipmentId,@RequestParam("prdctId") Long prdctId,RedirectAttributes attributes) {
		try {
			equipmentService.removeMaintenaceMaterialfromEquipment(EquipmentId, prdctId);
		} catch (RecordNotFoundException e) {
			attributes.addFlashAttribute("MaterialNotFound", "Material Not Found in The Maintenance List of Equipment :"+EquipmentId);
 			e.printStackTrace();
		}
		return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/eqpmntDetails/"+EquipmentId;
	}
	
	@PostMapping("/editEqpmntMntnceMatQty")
	public String editEquipmentMntnceMatrlQty(@RequestParam("eqpmntId") Long EquipmentId,@RequestParam("prdctId") Long prdctId,
			@RequestParam("quantity") Integer quantity,RedirectAttributes attributes) {
		try {
			equipmentService.changeEqpmntMatrlQuanity(EquipmentId, prdctId, quantity);
		} catch (RecordNotFoundException e) {
			attributes.addFlashAttribute("MaterialNotFound", "Material Not Found in The Maintenance List of Equipment :"+EquipmentId);
 			e.printStackTrace();
		}
		return "redirect:"+gateWayUrl+"/maintenance/eqpmnt/eqpmntDetails/"+EquipmentId;
	}
	
	
	
	
	@GetMapping("/list")
	public String getEquipmentList(
			@RequestParam(name="eqptId",required = false) String equipmentId,
			@RequestParam(name="mnfctr",required = false) String manufacturer,
			@RequestParam(name="dprt",required = false) String department,
			@RequestParam(name="ctgry",required = false) String category,
			@RequestParam(name="lctn",required = false) String location,
			@RequestParam(name="crtcly",required = false) String criticalityLevel,
			@RequestParam(name="oprSts",required = false) String operationalStatus,
			@RequestParam(name="mntncyFrcny",required = false) String maintenanceFrequency,
			@PageableDefault(page = 0,size = 10) Pageable pg,Map<String,Object> map) {
			
		if(equipmentId==null)
			equipmentId="";
		if(manufacturer==null)
			manufacturer="";
		if(department==null)
			department="";
		if(category==null)
			category="";
		if(location==null)
			location="";
		if(criticalityLevel==null)
			criticalityLevel="";
		if(operationalStatus==null)
			operationalStatus="";
		if(maintenanceFrequency==null)
			maintenanceFrequency="";
		map.put("equipmentId", equipmentId);
		map.put("manufacturer", manufacturer);
		map.put("department", department);
		map.put("category", category);
		map.put("location", location);
		map.put("criticalityLevel", criticalityLevel);
		map.put("operationalStatus", operationalStatus);
		map.put("maintenanceFrequency", maintenanceFrequency);

		Page<Equipment> page=equipmentService.getEquipmentList(equipmentId, manufacturer, department, category, location, criticalityLevel, operationalStatus, maintenanceFrequency, pg);	
		map.put("EquipmentList", page);
		return "EquipmentList";
	}
	
	@GetMapping("/eqpmntDetails/{id}")
	public String getEquipmentDetails(@PathVariable("id") Long id,Map<String,Object> map) {
		try {
			Equipment equipment=equipmentService.getEquipmentbyId(id);
			map.put("equipment", equipment);
		} catch (RecordNotFoundException e) {
 			e.printStackTrace();
		}		
		return "EquipmentDetails";
	}
	
	@GetMapping("/addBulkEqpmnts")
	public String getAddBulkEqmntsPage() {
		return "EquipmentBulkRgstr";
	}
	
	@PostMapping("/addBulkEqpmnts")
	public String uploadExcelFile(@RequestParam("equipmentList") MultipartFile file, Map<String,String> model,RedirectAttributes redirectAttributes) {		
		
		 
 	        // Get the file extension
	        String fileExtension = getFileExtension(file.getOriginalFilename());
	        try {
	            if (fileExtension.equals("xls")) {
	               throw new Exception();
	            } else if (fileExtension.equals("csv")) {
 	                List<Equipment> equipments = equipmentService.parseEquipmentCsv(file);
	                equipmentService.addAllEquipment(equipments);
	                redirectAttributes.addFlashAttribute("message", "CSV file uploaded and data saved successfully!");
	            } else {
	            	redirectAttributes.addFlashAttribute("message", "Please upload a valid .xls or .csv file!");
	            }
	        } catch (Exception e) {
	        	
	        	redirectAttributes.addFlashAttribute("message", "Error processing file ");
	        }
		 
        return "EquipmentBulkRgstr";
   }
   // Helper method to get file extension
   private String getFileExtension(String filename) {
       if (filename != null && filename.contains(".")) {
           return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
       }
       return "";
   }
	
	
	@ModelAttribute("LocationsList")
	public List<String> getLocationsList(){
		return equipmentService.getAllLocationsName();
	}
	@ModelAttribute("OperationalStatusList")
	public List<String> getOperationalStatusList(){
		return operationalStatusList;
	}
	@ModelAttribute("CriticalityLevelList")
	public List<String> getCriticalityLevelList(){
		return criticalityLevelList;
	}
	@ModelAttribute("MaintenanceFrequencyList")
	public List<String> getMaintenanceFrequencyList(){
		return maintenanceFrequencyList;
	}
	@ModelAttribute("DepartmentList")
	public List<String> getDepartmentListList(){
		return departmentList;
	}
	@ModelAttribute("CategoryList")
	public List<String> getCategoryListList(){
		return categoryList;
	}
	@ModelAttribute("ManfacturersList")
	public List<String> getManfacturersList(){
		return equipmentService.getManufacturersList();
	}
}










