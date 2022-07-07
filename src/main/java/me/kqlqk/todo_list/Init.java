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
 * ! restfull service v2 rest
 * ! add sign up button to login page v2 dev
 * ! improve remember me button v2 dev
 * ! add default values to db v2 dev
 * ! integrated tests v2+ dev
 */

