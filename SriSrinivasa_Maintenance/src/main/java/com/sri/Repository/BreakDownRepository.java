package com.sri.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sri.Entity.BreakDown;

import jakarta.transaction.Transactional;

public interface BreakDownRepository extends JpaRepository<BreakDown, Long> {
	
	@Transactional
	@Modifying
	@Query("update BreakDown set status =:status where breakDownId =:breakDownId")
	int updateBreakDownStatus(String status,Long breakDownId);
	
	List<BreakDown> findByStatus(String status);
	

}
