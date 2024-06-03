package com.cmux.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.cmux.chat.repository")
public class ChatApplication {
	private static final Logger logger = LoggerFactory.getLogger(ChatApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
		logger.info("The cmu-x chat system has started successfully!");
	}

}
