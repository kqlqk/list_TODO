package me.kqlqk.todo_list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Init {
    private static final Logger logger = LoggerFactory.getLogger(Init.class);
    public static long appStartTime;

    public static void main(String[] args) {
        appStartTime = System.currentTimeMillis();

        SpringApplication.run(Init.class, args);
        logger.info("App was started");
    }
}

/*
 * TODO:
 * 3.refactoring v1.5 improving
 * 4.sign up with google v1.5 dev
 */

