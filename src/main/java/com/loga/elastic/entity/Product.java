package com.loga.elastic.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

	private String id;
	private String title;
	private String description;
	private double price;
	private double discountPercentage;
	private double rating;
	private double stock;
	private String brand;
	private String category;
	private String thumbnail;
	private List<String> images;
}
