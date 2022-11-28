package com.loga.elastic.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.loga.elastic.entity.Product;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;

public class SearchUtils {

	public static long getTotalCount(SearchResponse<?> searchResponse) {
		return searchResponse.hits().total().value();
	}

	public static <T> Collection<T> convertSearchResult(SearchResponse<T> response) {
		List<T> listCollection = new LinkedList<>();
		if (response == null) {
			return listCollection;
		}
		HitsMetadata<T> metaData = response.hits();
		if (metaData == null) {
			return listCollection;
		}
		List<Hit<T>> hits = metaData.hits();

		for (Hit<T> hit : hits) {
			T t = hit.source();
			listCollection.add(t);
		}
		return listCollection;
	}

	public static Collection<String> convertCompletionResult(SearchResponse<Product> response) {
		List<String> listCollection = new LinkedList<>();
		if (response == null) {
			return listCollection;
		}
		HitsMetadata<Product> metaData = response.hits();
		if (metaData == null) {
			return listCollection;
		}
		List<Hit<Product>> hits = metaData.hits();

		for (Hit<Product> hit : hits) {
			Product p = hit.source();
			listCollection.add(p.getTitle());
		}
		return listCollection;
	}
}
