
package me.kqlqk.todo_list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of application
 * <p>
 * For more information please visit our <a href="https://github.com/kqlqk/list_TODO">github repository</a>
 */
@SpringBootApplication
public class Init {
    private static final Logger logger = LoggerFactory.getLogger(Init.class);

    /**
     * Application start time
     */
    public static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        SpringApplication.run(Init.class, args);
        logger.info("Todo_list was started");
    }
}