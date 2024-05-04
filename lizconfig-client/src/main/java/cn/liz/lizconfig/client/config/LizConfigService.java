package cn.liz.lizconfig.client.config;

import cn.liz.lizconfig.client.config.repository.LizRepository;

public interface LizConfigService {

    static LizConfigService getDefault(ConfigMeta configMeta) {
        LizRepository repository = LizRepository.getDefault(configMeta);
        return new LizConfigServiceImpl(repository.getConfig());
    }

    String[] getPropertyNames();

    String getProperty(String name);
}
