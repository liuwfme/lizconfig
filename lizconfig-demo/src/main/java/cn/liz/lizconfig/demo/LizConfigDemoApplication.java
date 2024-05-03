package cn.liz.lizconfig.demo;

import cn.liz.lizconfig.client.annotation.EnableLizConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@EnableConfigurationProperties({LizDemoConfig.class})
@SpringBootApplication
@EnableLizConfig
public class LizConfigDemoApplication {

    @Value("${liz.a}")
    private String a;

    @Autowired
    private LizDemoConfig config;

    @Autowired
    Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(LizConfigDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        return args -> {
            System.out.println(a);
            System.out.println(config.getA());
        };
    }
}
