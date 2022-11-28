package com.loga.elastic.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class RestClientConfig {

	@Bean
	public ElasticsearchClient getElasticClient() {
		RestClient restClient = RestClient.builder(new HttpHost("129.154.41.230", 9200))
				//.setHttpClientConfigCallback(clientBuilder -> clientBuilder.setProxy(new HttpHost("127.0.0.1", 8888)))
				.build();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(mapper));
		return new ElasticsearchClient(transport);
	}

}
