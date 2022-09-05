package me.kqlqk.todo_list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Init {
    private static final Logger logger = LoggerFactory.getLogger(Init.class);
    public static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        SpringApplication.run(Init.class, args);
        logger.info("Todo_list was started");
    }
}
/*
    oauth2
    authn(pass key apple)
    refactoring to microservices
    javadoc
 */