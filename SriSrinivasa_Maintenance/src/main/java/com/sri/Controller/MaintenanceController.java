package com.sri.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.DTO.New_Product_Req;
import com.sri.DTO.Prod_Inv_DTO_Product;
import com.sri.Entity.BreakDown;
import com.sri.Entity.Equipment;
import com.sri.Entity.MaintenanceTask;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Entity.MaterialRequiredWorkOrder;
import com.sri.Entity.WorkOrder;
import com.sri.Exception.RecordNotFoundException;
import com.sri.FeignClient.Inventory_Client;
import com.sri.Messaging.MessagePublisher;
import com.sri.Service.EquipmentService;
import com.sri.Service.MaintenanceService;
import com.sri.utils.EquipmetUtils;

import feign.FeignException;

@Controller
@RequestMapping("/maintenance/mntnce")
public class MaintenanceController {
	
	final EquipmentService equipmentService;
	
	@Autowired
	MaintenanceService maintenanceService;
	
	@Autowired
	Inventory_Client inventoryClient;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	MessagePublisher messagePublisher;
	
	@Value("${gateway.url}")
	String gateWayUrl;

    MaintenanceController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }
	
	@GetMapping("/mntncTasks")
	public String getMaintenanceTasks(Map<String,Object> map) {
		List<MaintenanceTask> taskList=maintenanceService.getMaintenanceTaskbyStatus("Pending");
		map.put("MaintenanceTaskList", taskList);
		return "MaintenanceTaskList";
	}
	
	@GetMapping("/registerWorkOrder/{equipmentId}/{maintenanceId}")
	public String getWorkOrderPage(@PathVariable("equipmentId") Long equipmentId,@PathVariable("maintenanceId") Long maintenanceId,Map<String,Object> map) {
		try {
			Equipment eqpmnt=equipmentService.getEquipmentbyId(equipmentId);			
 			WorkOrder workOrder=new WorkOrder();
			workOrder.setLocation(eqpmnt.getLocation());
			workOrder.setDepartment(eqpmnt.getDepartment());
			workOrder.setEquipmentName(eqpmnt.getName());
			workOrder.setStatus("Pending");
			workOrder.setEquipmentId(eqpmnt.getEquipmentId());
			workOrder.setMaintenanceInformation(eqpmnt.getMaintenanceInformation());
			workOrder.setMaterialRequired(eqpmnt.getMaterialRequiredEquipment().stream().map(material->EquipmetUtils.getMaterialRequiredWorkOrder(material)).collect(Collectors.toList()));
			workOrder.setMaintenanceTaskId(maintenanceId);
			workOrder.setMaintenanceType("Preventive");
			 
			map.put("workOrder",workOrder);	
			
			List<String> supervisorList=maintenanceService.getMaintenanceTeamSupervisorList();
			map.put("supervisorList", supervisorList);
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
		}
		return "WorkOrderRegister";
	}
	
	@PostMapping("/registerWorkOrder")
	public String registerWorkOrder(@ModelAttribute("workOrder") WorkOrder workOrder,@RequestParam(value = "submitWorkOrder", required = false) String submitWorkOrder,
            @RequestParam(value = "editMaterial", required = false) String editMaterial) {
		
		String workOrderId=maintenanceService.saveWorkOrder(workOrder);
 		if(workOrder.getMaintenanceTeam()!=null) {
 			try {
				maintenanceService.addMainteneceTeamWorkOrderId(workOrder.getMaintenanceTeam(), Long.parseLong(workOrderId));
			} catch (Exception ex) {
 			}  
		}
		
		if(editMaterial!=null)
			return "redirect:"+gateWayUrl+"/maintenance/mntnce/registerMaterialPage/"+workOrderId+"/"+workOrder.getEquipmentId();
		
		List<MaterialRequiredEquipment> materialList;
		try {
			materialList = equipmentService.getMaintenanceMaterialsbyId(workOrder.getEquipmentId());
			List<MaterialRequiredWorkOrder> workOrderMaterialList=materialList.stream().map(material->EquipmetUtils.getMaterialRequiredWorkOrder(material)).collect(Collectors.toList());
			workOrderMaterialList.forEach(material->material.setWorkOrder(workOrder));
			
			maintenanceService.updateWorkOrderMaterialListbyId(workOrderMaterialList, Long.parseLong(workOrderId));
			return "redirect:"+gateWayUrl+"/maintenance/mntnce/getWorkOrder/"+workOrderId;
		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}	
	}
	
	@GetMapping("/registerMaterialPage/{workOrderId}/{equipmemtId}")
	public String registerMaterialPage(@PathVariable Long workOrderId,
			@PathVariable Long equipmemtId,Map<String,Object> map) {
 		List<Map<String,String>> completeList=new ArrayList<Map<String,String>>();
		
		ResponseEntity<?> categoryResponseEntity= inventoryClient.getCategories();
		if(!categoryResponseEntity.getStatusCode().isError()) {
			List<String> categoryList=(List<String>) categoryResponseEntity.getBody();
			categoryList.forEach(category->{
				ResponseEntity<?> categoryProducts=inventoryClient.getSKUByCategory(category);
				if(!categoryProducts.getStatusCode().isError()) {
					Map<String,String> products=(Map<String,String>) categoryProducts.getBody();
					completeList.add(products);
				}
			});
			map.put("workOrderId", workOrderId);
			map.put("equipmemtId", equipmemtId);
			map.put("Products", completeList);
			map.put("CategoryList", categoryList);
		}else {
			return "ErrorInternalServer";
		}
		return "MaintenanceMaterial";
	}
	
	@PostMapping("/registerMaterialQuantityPage")
	public String addMaterialQuantityPage(@RequestParam("selectedProducts") List<String> selectedProducts,
								@RequestParam("equipmemtId") Long equipmemtId,
								@RequestParam("workOrderId") Long workOrderId,
								Map<String,Object> map) {		
		try {			
 			ResponseEntity<?> materialResponseEntity= inventoryClient.getListedProductBasedonSKU(selectedProducts);
			if(materialResponseEntity.getStatusCode().isError()) {
				return "ErrorInternalServer";
			}	
			List<?> products= (List<?>) materialResponseEntity.getBody();
			List<Prod_Inv_DTO_Product> productList=products.stream().map(item -> objectMapper.convertValue(item, Prod_Inv_DTO_Product.class)).collect(Collectors.toList());
			map.put("Products_List", productList);
			map.put("equipmemtId", equipmemtId);
			map.put("workOrderId", workOrderId);
			map.put("maintenanceType", maintenanceService.getMaintenanceType(workOrderId));
		}catch(FeignException feign) {
			feign.printStackTrace();
			return "ErrorInternalServer";
		}catch (Exception e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
		return "MaintenanceMaterialQuantity";
	}
	
	@PostMapping("/registerMaterialQuantity")
	public String addMaterialQuantity(@RequestParam("workOrderId") Long workOrderId,
								@RequestParam("equipmemtId") Long equipmemtId,
								@RequestParam(name="save",required=false) String productSave,
								@RequestParam Map<Object, Object> quantities) { 
		List<String> selectedProducts=new ArrayList<String>();
		for(Map.Entry<Object, Object> entry: quantities.entrySet()) {
			try {
				if(!entry.getKey().equals("save")&&!entry.getKey().equals("workOrderId")&&!entry.getKey().equals("equipmemtId"))
					selectedProducts.add((String) entry.getKey());
			} catch (Exception  e) {
				e.printStackTrace();
			}
		}
 		try {
			ResponseEntity<?> materialResponseEntity= inventoryClient.getListedProductBasedonSKU(selectedProducts);
			if(materialResponseEntity.getStatusCode().isError()) 
				return "ErrorInternalServer";
			List<?> products= (List<?>) materialResponseEntity.getBody();
			List<Prod_Inv_DTO_Product> productsList=products.stream().map(item -> objectMapper.convertValue(item, Prod_Inv_DTO_Product.class)).collect(Collectors.toList());
			
			List<MaterialRequiredWorkOrder> materialWorkOrderList=productsList.stream().map(prod->
				EquipmetUtils.getMaterialRequiredWorkOrderfromProd_Inv(prod)).collect(Collectors.toList());
			
			try {
				WorkOrder worKOrder=maintenanceService.getWorkOrderById(workOrderId);
				materialWorkOrderList.forEach(mat->{
		 			mat.setQuantityRequired(Integer.parseInt((String) quantities.get(mat.getSku())));
		 			mat.setWorkOrder(worKOrder);
				});
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
			
			maintenanceService.updateWorkOrderMaterialListbyId(materialWorkOrderList, workOrderId);
			if(productSave!=null) {
				List<MaterialRequiredEquipment> materialEquipmentList=productsList.stream().map(prod->
				EquipmetUtils.getMaterialRequiredEquipmentfromProd_Inv(prod)).collect(Collectors.toList());
			
				materialEquipmentList.forEach(mat->{
	 				mat.setQuantityRequired(Integer.parseInt((String) quantities.get(mat.getSku())));
					try {
						mat.setEquipment(equipmentService.getEquipmentbyId(equipmemtId));
					} catch (RecordNotFoundException e) {
						e.printStackTrace();
					}
				});
				
				equipmentService.updateMaintenanceMaterialsbyId(materialEquipmentList, equipmemtId);
			}
		
		}catch(Exception e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
	
		return "redirect:"+gateWayUrl+"/maintenance/mntnce/getWorkOrder/"+workOrderId;
	}
	
	@GetMapping("/getWorkOrder/{workOrderId}")
	public String getWorkOrderDetails(@PathVariable("workOrderId") Long workOrderId,Map<String,Object> map,Principal principal,Authentication authentication) {
		try {
			if(EquipmetUtils.hasRole(authentication, "MAINTENANCE_SUPERVISOR")) {
				String registeredSupervisor=maintenanceService.getWorkOrderMaintenanceTeamSupervisor(workOrderId);
				if(!registeredSupervisor.equalsIgnoreCase(principal.getName()))
					return "error-403";
			}
		}catch(Exception e) {
			return "error-403";
		}
		try {
			WorkOrder workOrder=maintenanceService.getWorkOrderById(workOrderId);
			map.put("workOrder", workOrder);
 		} catch (RecordNotFoundException e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}
		return "WorkOrderDetails";
	}
	
	//BreakDown or Corrective
	@GetMapping("/breakdown")
	public String getBreakDownPage(@ModelAttribute("breakdown") BreakDown breakdown){
		return "Breakdown";
	}
	
	@PostMapping("/breakdown")
	public String registerBreakdown(@ModelAttribute("breakdown") BreakDown breakdown,Map<String,Object> map,RedirectAttributes redirectAttributes,Principal principal){
	    try {
	    	equipmentService.getMaintenanceFrequencyById(breakdown.getEquipmentId());
	    	breakdown.setStatus("Created");
	    	breakdown.setReportedBy(principal.getName());
	    	maintenanceService.saveBreakDown(breakdown);
	    }catch(RecordNotFoundException e) {
	    	redirectAttributes.addFlashAttribute("EquipmentNotFound","Equipment Not found "+breakdown.getEquipmentId());
			return "redirect:"+gateWayUrl+"/maintenance/mntnce/breakdown";	
	    }
    	redirectAttributes.addFlashAttribute("Success_Fully","Successfully BreakDown Raised ");
	    return "redirect:"+gateWayUrl+"/maintenance/mntnce/breakdown";
	}
	
	@GetMapping("/breakdownlist")
	public String getBreakDownList(Map<String,Object> map){
		List<BreakDown> breakdownsByStatus = maintenanceService.findBreakdownsByStatus("Created");
		map.put("breakDownList", breakdownsByStatus);
		return "BreakdownList";
	}
	
	@GetMapping("/approveBreakdown/{breakDownId}")
	public String registerBreakdown(@PathVariable("breakDownId") Long breakDownId, @ModelAttribute("workOrder") WorkOrder workOrder,Map<String,Object> map,Principal principal){
	    try {
			BreakDown breakdown=maintenanceService.getBreakDownbyId(breakDownId);
			Equipment eqpmnt = equipmentService.getEquipmentbyId(breakdown.getEquipmentId());
			workOrder.setEquipmentId(breakdown.getEquipmentId());
			workOrder.setLocation(eqpmnt.getLocation());
			workOrder.setDepartment(eqpmnt.getDepartment());
			workOrder.setEquipmentName(eqpmnt.getName());
			workOrder.setMaintenanceInformation(eqpmnt.getMaintenanceInformation());
			workOrder.setMaintenanceType("Corrective");
			workOrder.setBreakDownId(breakDownId);
			workOrder.setStatus("Created");
			workOrder.setFailureReport(breakdown.getFailureReport());
			workOrder.setPriorityLevel(breakdown.getPriorityLevel());
			workOrder.setReportedBy(breakdown.getReportedBy());
			workOrder.setBreakDownTime(breakdown.getBreakDownTime());
			workOrder.setApprovedBy(principal.getName());
			map.put("workOrder", workOrder);
			
			List<String> supervisorList=maintenanceService.getMaintenanceTeamSupervisorList();
			map.put("supervisorList", supervisorList);
		}catch (RecordNotFoundException e) {
			e.printStackTrace();
			return "ErrorInternalServer";
		}			
		return "WorkOrderRegister";
	}
	
	@GetMapping("/workOrderList")
	public String getWorkOrderList(@RequestParam(name = "equipmentId",required = false) Long equipmentId,
			@RequestParam(name = "Type",required = false) String maintenanceType,
			@RequestParam(name = "tchcn",required = false) String technician,
			@RequestParam(name = "prtyLvl",required = false) String priorityLevel,
			@RequestParam(name = "wrkOrdrId",required = false) Long workOrderId,
			@RequestParam(name = "sts",required = false) String status,
			@PageableDefault(page = 0,size = 4) Pageable pageable,
			Map<String,Object> map,Principal principal,Authentication authentication) {
		if(maintenanceType==null)
			maintenanceType="";
		if(technician==null)
			technician="";
		if(priorityLevel==null)
			priorityLevel="";
		if(status==null)
			status="";
		
		map.put("maintenanceType", maintenanceType);
		map.put("technician", technician);
		map.put("priorityLevel", priorityLevel);
		map.put("status", status);
		try {
			if(EquipmetUtils.hasRole(authentication, "MAINTENANCE_SUPERVISOR")) {
				technician=principal.getName();
			}
		}catch(Exception e) {
			return "error-403";
		}
		Page<WorkOrder> page=maintenanceService.getWorkOrder(equipmentId,workOrderId,maintenanceType,priorityLevel,status,technician,pageable);
		map.put("WorkOrderList", page);
		return "WorkOrderList";
	}
	
	//New Product Request
	@GetMapping("/requestNewMaterialPage/{workOrderId}/{equipmemtId}")
	public String getNewPrdouct(@PathVariable("workOrderId") Long workOrderId,@PathVariable("equipmemtId") Long equipmemtId,Map<String,Object> map) {
		map.put("equipmemtId", equipmemtId);
		New_Product_Req prod=new New_Product_Req();
		prod.setWorkOrderId(workOrderId);
		map.put("NewProduct", prod);
		return "NewProductEntry";
	}
	
	@GetMapping("/newProductReqList")
	public String getNewReqList(@RequestParam(name="workOrderId",required = false) Long workOrderId,@PageableDefault(page = 0,size = 10) Pageable pg,Map<String,Object> map) {
		Page<New_Product_Req> prodList=null;
		if(workOrderId!=null) {
			prodList=maintenanceService.getNew_Product_ReqByWorkOrder(workOrderId, pg);
		}else {
			prodList=maintenanceService.getNew_Product_ReqList(pg);
		}
		map.put("selectedworkOrderId", workOrderId);
		map.put("newProductRequests", prodList);
		return "NewProductList";
	}
	
	//Messaging
	@PostMapping("/requestNewMaterial")
	public String registerNewProduct(@RequestParam("equipmemtId") Long equipmemtId,@ModelAttribute("NewProduct") New_Product_Req prod,RedirectAttributes redirectAttributes,Principal principal) {
 		prod.setRequestedBy(principal.getName());
 		messagePublisher.MessageForNewProduct(prod);
		redirectAttributes.addFlashAttribute("NewProductRequest","Message Send to Inventory manager");
		return "redirect:"+gateWayUrl+"/maintenance/mntnce/registerMaterialPage/"+prod.getWorkOrderId()+"/"+equipmemtId;
	}
}







