package cn.liz.lizconfig.client.config.repository;

import cn.liz.lizconfig.client.config.ConfigMeta;

import java.util.Map;

public interface LizRepository {

    static LizRepository getDefault(ConfigMeta configMeta) {
        return new LizRepositoryImpl(configMeta);
    }

    Map<String, String> getConfig();

}
