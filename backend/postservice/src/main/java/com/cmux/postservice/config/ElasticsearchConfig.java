package com.cmux.postservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    
    
    @Bean
    public RestClient restClient() {
        // Configure the credentials provider to use your Elasticsearch credentials
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("elastic", "2JeXn31HFDxoD3iMkX5v")); 

        // Build the RestClient using the credentials provider
        return RestClient.builder(new HttpHost("elasticsearch-service", 9200, "http")) 
                .setHttpClientConfigCallback(httpClientBuilder -> 
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        // Create a new transport layer using the RestClient and Jackson mapper
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        // Create the ElasticsearchClient using the transport layer
        return new ElasticsearchClient(transport);
    }
}
