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
        logger.info("Todo_list was started");
    }
}
/*
 * TODO:
 *  check registrationDTO validation
 *  check if user has necessary cookies || headers auto redirect to home page
 *  change npe to iae
 *  add sign up button to login page v2.5 dev
 *  improve remember me button v2.5 dev
 */

