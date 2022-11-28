package com.loga.elastic.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pageable {
	
	private int page;
	private int size;
	
	public Pageable(int page, int size) {
		this.page=page;
		this.size=size;
	}
	
}
