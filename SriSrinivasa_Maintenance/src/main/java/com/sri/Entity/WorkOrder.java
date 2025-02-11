package com.sri.Entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
public class WorkOrder {

	@Id
	@SequenceGenerator(name = "workOrderSequence",allocationSize = 1)
	@GeneratedValue(generator = "workOrderSequence")
	private Long workOrderId;
	
	private Long equipmentId;
	private Long maintenanceTaskId;
	private Long breakDownId;
	@Lob
	private String taskDescription;
	private String priorityLevel;
	
	
	private String maintenanceType;
	private String status;
	@Lob
	private String specialInstructions;
	private String department;
	private String equipmentName;
	private String impact;
	private String location;
	
	@Lob
	private String maintenanceInformation;
	@Lob
	private String failureReport;
	
	private String maintenanceTeam;
	@Lob
	private String technicianFeedback;
	
	private String reportedBy;
	private String approvedBy;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date breakDownTime;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "workOrder")
	private List<MaterialRequiredWorkOrder> materialRequired;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date orderEntryDate;
}
