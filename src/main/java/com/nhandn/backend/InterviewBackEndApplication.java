package com.nhandn.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class InterviewBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewBackEndApplication.class, args);
    }

}
