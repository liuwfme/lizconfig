package cn.liz.lizconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@Slf4j
public class LizConfigServiceImpl implements LizConfigService {

    ApplicationContext applicationContext;

    Map<String, String> config;

    public LizConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.applicationContext = applicationContext;
        this.config = config;
    }

    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    public String getProperty(String name) {
        return config.get(name);
    }

    @Override
    public void onChange(ChangeEvent event) {
        this.config = event.config();
        if (!config.isEmpty()) {
            log.info(" ====== fire an EnvironmentChangeEvent keys : {}", config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
