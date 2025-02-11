package com.sri.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sri.Entity.Location;

public interface LocationRepository extends JpaRepository<Location, String> {
	
	@Query("select locationName from Location")
	List<String> getAllLocationNames();

}
