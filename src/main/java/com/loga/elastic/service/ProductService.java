package com.loga.elastic.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.loga.elastic.entity.Product;
import com.loga.elastic.utils.SearchUtils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements ElasticsearchRepository<Product, String, ElasticsearchClient> {

	private final ElasticsearchClient client;

	private static String INDEX = "products";

	@Override
	public ElasticsearchClient getClient() {
		return this.client;
	}

	@Override
	public String getIndex() {
		return INDEX;
	}

	@Override
	public Class<Product> getEntity() {
		return Product.class;
	}

	@Override
	public void saveAll(Collection<Product> collection) throws Exception {
		BulkRequest.Builder br = new BulkRequest.Builder();
		for (Product product : collection) {
			br.operations(op -> op.index(idx -> idx.index(getIndex()).id(product.getId()).document(product)));
		}
		getClient().bulk(br.build());
	}

	public Collection<String> getSuggestion(String keyword) throws Exception {
		SearchRequest.Builder builder = getSearchRequestBuilder();
		builder.query(QueryBuilders.multiMatch(mm -> mm.type(TextQueryType.BoolPrefix).query(keyword).fields(Arrays.asList("title", "title._2gram", "title._3gram"))));
		SearchRequest request = builder.build();
		return SearchUtils.convertCompletionResult(search(request));
	}

}
