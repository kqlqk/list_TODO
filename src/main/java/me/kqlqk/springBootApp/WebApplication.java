package me.kqlqk.springBootApp;

import me.kqlqk.springBootApp.util.SessionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication extends SessionUtil {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    /**
    * TODO:auth with google, admin menu, front-end for home page
    * FIXME:delete hibernate.cfg.xml
    *
    * */
}
