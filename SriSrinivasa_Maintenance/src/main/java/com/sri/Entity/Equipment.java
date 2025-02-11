package com.sri.Entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

	@Id
	@SequenceGenerator(name = "equipmentSequence",allocationSize = 1)
	@GeneratedValue(generator = "equipmentSequence")
	private Long equipmentId;

	private String name;
	private String manufacturer;
	private String modelNumber;
	private String department;
	private String category;
    @Column(length = 3500)
	private String warrantyInformation;
	private String criticalityLevel;
	private String operationalStatus;
	private int operatingHours;
	private double cost;
	@Column(length = 3500)
	private String notes;

	private String location;
	
	private String maintenanceFrequency;
	@Column(length = 3500)
	private String maintenanceInformation;
	private LocalDate nextMaintenanceDate;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "equipment")
	private List<MaterialRequiredEquipment> materialRequiredEquipment;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date entryDate;
}
