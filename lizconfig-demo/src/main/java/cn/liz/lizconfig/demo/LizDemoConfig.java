package cn.liz.lizconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "liz")
public class LizDemoConfig {
    String a;
}
