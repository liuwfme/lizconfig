package cn.liz.lizconfig.client.config.spring;

import cn.liz.lizconfig.client.config.LizConfigService;
import org.springframework.core.env.EnumerablePropertySource;

public class LizPropertySource extends EnumerablePropertySource<LizConfigService> {

    public LizPropertySource(String name, LizConfigService source) {
        super(name, source);
    }

    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
