package ru.msu.cmc.webprack;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class WebprackApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebprackApplication.class, args);
    }
}
