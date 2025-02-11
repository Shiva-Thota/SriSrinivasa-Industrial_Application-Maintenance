package com.sri.utils;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.sri.DTO.Prod_Inv_DTO_Product;
import com.sri.Entity.MaterialRequiredEquipment;
import com.sri.Entity.MaterialRequiredWorkOrder;

public class EquipmetUtils {

	public static MaterialRequiredWorkOrder getMaterialRequiredWorkOrder(MaterialRequiredEquipment materialEquipment){
		MaterialRequiredWorkOrder equipmentWorkOrder=new MaterialRequiredWorkOrder();
		equipmentWorkOrder.setCategory(materialEquipment.getCategory());
		equipmentWorkOrder.setDescription(materialEquipment.getDescription());
		equipmentWorkOrder.setName(materialEquipment.getName());
		equipmentWorkOrder.setProductId(materialEquipment.getProductId());
		equipmentWorkOrder.setQuantityRequired(materialEquipment.getQuantityRequired());
		equipmentWorkOrder.setSku(materialEquipment.getSku());
		equipmentWorkOrder.setInventoryLocations(materialEquipment.getInventoryLocations());
		return equipmentWorkOrder;
	}
	public static MaterialRequiredEquipment getMaterialRequiredEquipment(MaterialRequiredWorkOrder equipmentWorkOrder) {
		MaterialRequiredEquipment materialEquipment=new MaterialRequiredEquipment();
		materialEquipment.setCategory(equipmentWorkOrder.getCategory());
		materialEquipment.setDescription(equipmentWorkOrder.getDescription());
		materialEquipment.setName(equipmentWorkOrder.getName());
		materialEquipment.setProductId(equipmentWorkOrder.getProductId());
		materialEquipment.setQuantityRequired(equipmentWorkOrder.getQuantityRequired());
		materialEquipment.setSku(equipmentWorkOrder.getSku());
		materialEquipment.setInventoryLocations(equipmentWorkOrder.getInventoryLocations());
		return materialEquipment;
	}
	public static MaterialRequiredWorkOrder getMaterialRequiredWorkOrderfromProd_Inv(Prod_Inv_DTO_Product product) {
		MaterialRequiredWorkOrder equipmentWorkOrder=new MaterialRequiredWorkOrder();
		equipmentWorkOrder.setCategory(product.getCategory());
		equipmentWorkOrder.setDescription(product.getDescription());
		equipmentWorkOrder.setName(product.getProductName());
		equipmentWorkOrder.setProductId(product.getProductId());
	 
		equipmentWorkOrder.setInventoryLocations(product.getLocation());
		equipmentWorkOrder.setSku(product.getSku());
 		return equipmentWorkOrder;
	}
	public static MaterialRequiredEquipment getMaterialRequiredEquipmentfromProd_Inv(Prod_Inv_DTO_Product product) {
		MaterialRequiredEquipment materialEquipment=new MaterialRequiredEquipment();
		materialEquipment.setCategory(product.getCategory());
		materialEquipment.setDescription(product.getDescription());
		materialEquipment.setName(product.getProductName());
		materialEquipment.setProductId(product.getProductId());

		materialEquipment.setInventoryLocations(product.getLocation());
		materialEquipment.setSku(product.getSku());
		return materialEquipment;
	}
	public static boolean hasRole(Authentication authentication, String role) {
		List<GrantedAuthority> authorites=  (List<GrantedAuthority>) authentication.getAuthorities();
		for(GrantedAuthority auth:authorites) {
			if(auth.getAuthority().equalsIgnoreCase(role))
				return true;
		}
		return false;
	}
	
	
}
