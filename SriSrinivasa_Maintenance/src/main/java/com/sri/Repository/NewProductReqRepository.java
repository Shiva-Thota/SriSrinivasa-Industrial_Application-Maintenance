package com.sri.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sri.DTO.New_Product_Req;
import java.util.List;



public interface NewProductReqRepository extends JpaRepository<New_Product_Req, Long>{
	
	Page<New_Product_Req> findByWorkOrderId(Long workOrderId,Pageable pg);
	
}
