package com.sri.DTO;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class New_Product_Req {
	@Id
	@SequenceGenerator(name = "newProductRequestSequence",allocationSize = 1)
	@GeneratedValue(generator = "newProductRequestSequence")
	private Long newProductReqId;
	
	private Long requestId;
	private Long productId;
	private String sku;
	private String name;
	private String priority;
	
	@Column(length = 1500)
	private String description;
	
	@Column(length = 500)
	private String purpose;
	
	@Column(length =1500)
	private String technicalSpecifications;
	
	private Integer leadTime;
	private String status;
	
	private String requestedBy;
	private String approvedBy;
	private Integer productionId;
	
	@Column(length = 500)
	private String inventoryManagerComments;
	
	@Column(length = 500)
	private String productRequestComments;
	private Long workOrderId;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date entryDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;
}
