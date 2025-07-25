package com.onetouch.delinight;

import com.onetouch.delinight.Config.OpenAiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing  // 감사 활성화
@EnableScheduling
@EnableConfigurationProperties(OpenAiConfig.class)
public class DelinightApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelinightApplication.class, args);
    }

}
