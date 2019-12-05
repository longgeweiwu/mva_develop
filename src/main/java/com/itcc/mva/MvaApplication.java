package com.itcc.mva;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.itcc.mva.mapper")
@SpringBootApplication
public class MvaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvaApplication.class, args);
    }

}
