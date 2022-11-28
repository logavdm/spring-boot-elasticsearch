package com.loga.elastic.model;

import java.util.List;

import com.loga.elastic.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	
	private List<Product> products;

}
