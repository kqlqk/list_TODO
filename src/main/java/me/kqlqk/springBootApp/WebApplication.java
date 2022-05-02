package me.kqlqk.springBootApp;

import me.kqlqk.springBootApp.util.SessionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication extends SessionUtil {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
