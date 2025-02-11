package com.sri.Entity;

import java.time.LocalDate;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MaintenanceTask {

	@Id
	@SequenceGenerator(name = "maintenanceTaskSequence",allocationSize = 1)
	@GeneratedValue(generator = "maintenanceTaskSequence")
	private Long maintenanceTaskId;
	private String status;
	private Long EquipmentId;
	private String criticalityLevel;
	private LocalDate scheduledDate;
	private String maintenanceFrequency;
	private String location;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date taskEntryDate;
}
