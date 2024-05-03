package cn.liz.lizconfig.client.config;

import java.util.Map;

public class LizConfigServiceImpl implements LizConfigService {

    Map<String, String> config;

    public LizConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }

    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    public String getProperty(String name) {
        return config.get(name);
    }
}
