package com.itcc.mva;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients({"com.itcc.mva.feign"})
@MapperScan("com.itcc.mva.mapper")
@SpringBootApplication
public class MvaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvaApplication.class, args);
    }

}
