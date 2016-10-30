package org.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by ILNUR on 29.10.2016.
 */


@SpringBootApplication
@EnableAutoConfiguration
public class Main {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Main.class, args);
//        IUserDao userDao = app.getBean(IUserDao.class);
//        User user = userDao.findByName("Ilnur");
    }
}
