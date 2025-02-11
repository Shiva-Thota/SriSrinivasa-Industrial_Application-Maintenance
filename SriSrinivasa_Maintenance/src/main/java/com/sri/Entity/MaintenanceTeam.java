package com.sri.Entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MaintenanceTeam {
	
	@Id
	private String supervisor;
	
	private String manager;
	@ElementCollection(fetch =FetchType.EAGER)
	@CollectionTable(name="MaintenanceTeamMates",
		joinColumns = @JoinColumn(name="supervisor",referencedColumnName = "supervisor"))
	private List<String> teamMates;
	@ElementCollection(fetch =FetchType.EAGER)
	@CollectionTable(name="MaintenanceTeamWorkOrders",
		joinColumns = @JoinColumn(name="supervisor",referencedColumnName = "supervisor"))
	private List<Long> WorkOrderIds;
	
	private String status;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date")
	private Date entryDate;
	
	
}
