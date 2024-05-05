package cn.liz.lizconfig.client.config;

import cn.liz.lizconfig.client.repository.ChangeListener;
import cn.liz.lizconfig.client.repository.LizRepository;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface LizConfigService extends ChangeListener {

    static LizConfigService getDefault(ApplicationContext applicationContext, ConfigMeta configMeta) {
        LizRepository repository = LizRepository.getDefault(configMeta);
        Map<String, String> config = repository.getConfig();
        LizConfigService configService = new LizConfigServiceImpl(applicationContext, config);
        repository.addListener(configService);
        return configService;
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
