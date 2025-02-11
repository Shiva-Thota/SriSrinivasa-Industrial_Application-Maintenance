package com.sri.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MaterialRequiredWorkOrder {

	@Id
	@SequenceGenerator(name = "materialSequence",allocationSize = 1)
	@GeneratedValue(generator = "materialSequence")
	private Long materialId;
	
	private String sku;
	private String name;
	private Long productId;
	private String category;
	private String description; 
	private Integer quantityRequired;
	private List<String> inventoryLocations;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "workOrderId",referencedColumnName = "workOrderId")
	private WorkOrder workOrder;

	@Override
	public String toString() {
		return productId + ",";
	}
	@Override
	public boolean equals(Object obj) {
		try {
			MaterialRequiredWorkOrder mat=(MaterialRequiredWorkOrder) obj;
			return Long.compare(this.getProductId(), mat.getProductId())==0?true:false;
		}catch(Exception e) {
			return false;
		}

	}
	
}
