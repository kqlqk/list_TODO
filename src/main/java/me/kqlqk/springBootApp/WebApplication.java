package me.kqlqk.springBootApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class WebApplication {
    public static long appTime;
    public static void main(String[] args) {
        appTime = System.currentTimeMillis();

        SpringApplication.run(WebApplication.class, args);
    }
}
/**
 * TODO:
 * 1.sign up with google v1.2
 * 3.admin menu(after 1st release) v1.1
 * 5.password recovery v1.2
 * 6.log system v1.1
 */

