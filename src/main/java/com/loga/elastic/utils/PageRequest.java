package com.loga.elastic.utils;

public class PageRequest extends Pageable{
	
	public PageRequest(int page,int size) {
		super(page,size);
	}

	public static PageRequest ofSize(int pageSize) {
		return PageRequest.of(0, pageSize);
	}
	
	public static PageRequest of(int page, int size) {
		return new PageRequest(page, size);
	}
}
