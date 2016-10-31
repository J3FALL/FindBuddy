package org.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Application.class, args);
//        UserDao userDao = app.getBean(UserDao.class);
//        User user = userDao.findByName("Ilnur");
    }
}