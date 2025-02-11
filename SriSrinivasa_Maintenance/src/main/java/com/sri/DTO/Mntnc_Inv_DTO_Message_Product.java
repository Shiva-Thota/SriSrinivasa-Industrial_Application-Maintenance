package com.sri.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

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
public class Mntnc_Inv_DTO_Message_Product implements Serializable {

	private Long equipmentId;
	private String equipmentLocation;
	private Map<Long,Integer> materialandQuantity;
	private Date deadLineForDelivery;
	private String maintenanceManagerId;
	private String maintenanceManagerComments;
	private Long workOrderId;
}

