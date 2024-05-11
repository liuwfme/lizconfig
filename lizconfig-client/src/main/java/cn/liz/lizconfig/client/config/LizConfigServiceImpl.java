package cn.liz.lizconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<String> keys = calcChangeKeys(this.config, event.config());
        if (keys.isEmpty()) {
            log.info(" ====== keys no change , update ignore.");
            return;
        }
        this.config = event.config();
        if (config.isEmpty()) {
            return;
        }
        log.info(" ====== fire an EnvironmentChangeEvent keys : {}", keys);
        applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
    }

    private Set<String> calcChangeKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
        if (oldConfigs.isEmpty()) return newConfigs.keySet();
        if (newConfigs.isEmpty()) return oldConfigs.keySet();
        Set<String> news = newConfigs.keySet().stream()
                .filter(key -> !newConfigs.get(key).equals(oldConfigs.get(key)))
                .collect(Collectors.toSet());
        oldConfigs.keySet().stream()
                .filter(key -> !newConfigs.containsKey(key))
                .forEach(news::add);
        return news;
    }

}
