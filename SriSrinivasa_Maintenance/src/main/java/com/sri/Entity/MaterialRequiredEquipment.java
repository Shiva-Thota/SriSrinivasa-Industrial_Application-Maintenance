package com.sri.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequiredEquipment {

	@Id
	@SequenceGenerator(name = "materialSequence",allocationSize = 1)
	@GeneratedValue(generator = "materialSequence")
	private Long materialId;
	
	private String Sku;
	private String name;
	private Long productId;
	private String category;
	private String description;
	private Integer quantityRequired;
	private List<String> inventoryLocations;

	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "equipmentID",referencedColumnName = "equipmentId")
	private Equipment equipment;

	@Override
	public boolean equals(Object obj) {
		try {
			MaterialRequiredEquipment mat=(MaterialRequiredEquipment) obj;
			return Long.compare(this.getProductId(), mat.getProductId())==0?true:false;
		}catch(Exception e) {
			return false;
		}

	}

	@Override
	public String toString() {
		return  productId + ",";
	}
	
	
	
	
}
