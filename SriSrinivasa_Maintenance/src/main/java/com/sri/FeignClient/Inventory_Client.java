package com.sri.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="SriSrinivasaInventory")
public interface Inventory_Client {
	
	@GetMapping("/invntry/getCategories")
	public ResponseEntity<?> getCategories();
		
	@GetMapping("/invntry/getProductByCategory/{category}")
	public ResponseEntity<?> getProductByCategory(@PathVariable String category);
	
	@GetMapping("/invntry/getSKUByCategory/{category}")
	public ResponseEntity<?> getSKUByCategory(@PathVariable String category);
	
	@GetMapping("/invntry/getProductById/{id}")
	public ResponseEntity<?> getProductBasedonID(@PathVariable Long id);
	
	@PostMapping("/invntry/getListedProductsbyId")
	public ResponseEntity<?> getListedProducts(@RequestBody List<Long> productIdList);
	
	@PostMapping("/invntry/getListedProductBySKU")
	public ResponseEntity<?> getListedProductBasedonSKU(@RequestBody List<String> productSKUList);

}
