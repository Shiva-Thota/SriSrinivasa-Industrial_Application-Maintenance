package com.sri.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BreakDown {
	
	@Id
	@SequenceGenerator(name = "breakDownSequence",allocationSize = 1)
	@GeneratedValue(generator = "breakDownSequence")
	private Long breakDownId;
	
	private Long equipmentId;
	private String reportedBy;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date breakDownTime;
	
	private String status;
	
	private String priorityLevel;
	@Lob
	private String failureReport;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date orderEntryDate;

}
