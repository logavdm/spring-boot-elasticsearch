package com.loga.elastic.service;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collection;

import com.loga.elastic.utils.Pageable;
import com.loga.elastic.utils.SearchUtils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;

public interface ElasticsearchRepository<T, ID, V extends ElasticsearchClient> {

	@Slf4j
	final class Log {
	}

	Class<T> getEntity();

	V getClient();

	String getIndex();

	default Collection<T> findAll() throws Exception {
		SearchRequest.Builder builder = getSearchRequestBuilder();
		builder.query(QueryBuilders.matchAll().build()._toQuery());
		builder.size((int) countAll());
		return SearchUtils.convertSearchResult(search(builder.build()));
	}

	default Collection<T> findAll(Pageable page) throws Exception{		
		if(page==null) {
			Log.log.error("Page instance must not be null");
			throw new Exception("Error");
		}		
		SearchRequest.Builder builder = getSearchRequestBuilder();
		builder.query(QueryBuilders.matchAll().build()._toQuery());
		builder.from(page.getPage()*page.getSize());
		builder.size(page.getSize());		
		return SearchUtils.convertSearchResult(search(builder.build()));
	}

	default T findById(String id) throws Exception {
		GetResponse<T> response = getClient().get(g -> g.index(getIndex()).id(id), getEntity());
		if (!response.found())
			return null;
		return response.source();
	}

	default void deleteAll() throws Exception {
		DeleteByQueryRequest request = DeleteByQueryRequest
				.of(r -> r.index(Arrays.asList(getIndex())).query(QueryBuilders.matchAll().build()._toQuery()));
		getClient().deleteByQuery(request);
	}

	default void deleteById(String id) throws Exception {
		DeleteRequest request = DeleteRequest.of(d -> d.index(getIndex()).id(id));
		getClient().delete(request);
	}

	void saveAll(Collection<T> collection) throws Exception;

	default long countAll() throws Exception {
		SearchRequest.Builder builder = getSearchRequestBuilder();
		builder.query(QueryBuilders.matchAll().build()._toQuery());
		builder.size(0);
		return SearchUtils.getTotalCount(search(builder.build()));
	}

	default long count(SearchRequest request) throws Exception {
		SearchRequest.Builder builder = getSearchRequestBuilder();
		builder.query(request.query());
		builder.size(0);
		return SearchUtils.getTotalCount(search(builder.build()));
	}

	default Builder getSearchRequestBuilder() {
		SearchRequest.Builder builder = new SearchRequest.Builder();
		builder.index(Arrays.asList(getIndex()));
		return builder;
	}

	default SearchResponse<T> search(SearchRequest request) throws Exception {
		Log.log.debug(request.toString());
		return getClient().search(request, getEntity());
	}
	
	default SearchResponse<?> search(SearchRequest request,Class<?> clz) throws Exception {
		Log.log.debug(request.toString());
		return getClient().search(request,clz);
	}

	default SearchResponse<T> search(Query query) throws Exception {
		Log.log.debug(query.toString());
		return getClient().search(s -> s.index(getIndex()).query(query), getEntity());
	}
	
	default SearchResponse<?> search(Query query,Class<?> clz) throws Exception {
		Log.log.debug(query.toString());
		return getClient().search(s -> s.index(getIndex()).query(query), clz);
	}

	default SearchResponse<T> search(String query) throws Exception {
		Log.log.debug(query);
		return getClient().search(
				s -> s.index(getIndex()).query(q -> q.withJson(new ByteArrayInputStream(query.getBytes()))),
				getEntity());
	}

}
