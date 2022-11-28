package com.loga.elastic.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.loga.elastic.entity.Product;
import com.loga.elastic.model.ProductResponse;
import com.loga.elastic.service.ProductService;
import com.loga.elastic.utils.Pageable;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/fetchAll")
	public List<Product> getAllProducts() throws Exception {
		return (List<Product>) productService.findAll();
	}
	
	@GetMapping("/fetchAll/page")
	public List<Product> getAllProductsWithPageable(@RequestBody Pageable pageable) throws Exception {
		return (List<Product>) productService.findAll(pageable);
	}

	@GetMapping("/fetch/{id}")
	public Product fetchById(@PathVariable("id") String id) throws Exception {
		return productService.findById(id);
	}

	@GetMapping("/delete/{id}")
	public void deleteById(@PathVariable("id") String id) throws Exception {
		productService.deleteById(id);
	}

	@GetMapping("/deleteAll")
	public void deleteAll() throws Exception {
		productService.deleteAll();
	}

	@GetMapping("/loadData")
	public void loadData(@RequestParam("size") Long size) throws Exception {
		RestTemplate restClient = new RestTemplate();
		ResponseEntity<ProductResponse> response = restClient.getForEntity("https://dummyjson.com/products?limit=" + size, ProductResponse.class);
		productService.saveAll(response.getBody().getProducts());
	}
	
	@GetMapping("/countAll")
	public long countAll() throws Exception {			
		return productService.countAll();
	}

	@GetMapping("/autocomplete")
	public Collection<String> autoCompleteSuggestion(@RequestParam("keyword") String keyword, Pageable page) throws Exception {
		return productService.getSuggestion(keyword);
	}

}
