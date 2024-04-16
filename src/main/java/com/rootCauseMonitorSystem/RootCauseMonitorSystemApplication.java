package com.rootCauseMonitorSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling // 开启定时任务
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RootCauseMonitorSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RootCauseMonitorSystemApplication.class, args);
    }

}
