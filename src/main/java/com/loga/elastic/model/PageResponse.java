package com.loga.elastic.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponse<T> {
	private int size;
	private Long totalElements;
	private int totalPages;
	private int currentPage;
	private List<T> content;
}
