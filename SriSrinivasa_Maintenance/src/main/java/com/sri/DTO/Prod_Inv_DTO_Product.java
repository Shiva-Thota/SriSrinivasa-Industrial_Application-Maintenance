package com.sri.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Prod_Inv_DTO_Product {
	
	private	Long productId;
	private	String sku;
	private	String productName;
	private	String category;
	private	String description;
	private	String unitOfMeasure;
	private Long price;
	private	Long quantity;
	private List<String> location;
 
	
}
