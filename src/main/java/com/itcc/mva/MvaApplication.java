package com.itcc.mva;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.itcc.mva.mapper")
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@SpringBootApplication
public class MvaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvaApplication.class, args);
    }

}
