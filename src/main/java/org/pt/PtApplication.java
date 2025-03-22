package org.pt;

import org.mybatis.spring.annotation.MapperScan;
import org.pt.tracker.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("org.pt.mapper")
public class PtApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtApplication.class, args);
    }

    @Autowired
    private TrackerService trackerService;

    @Bean
    public CommandLineRunner startTracker() {
        return args -> {
            // 启动Tracker
            trackerService.startTracker();
            System.out.println("Tracker started on /announce");
        };
    }
}
