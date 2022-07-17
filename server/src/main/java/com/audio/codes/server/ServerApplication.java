package com.audio.codes.server;

import com.audio.codes.server.controller.AudioCodesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class.getName());
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		logger.info("Server is running!!!");
	}

}
