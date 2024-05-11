package cn.liz.lizconfig.client.config;

import org.springframework.core.env.EnumerablePropertySource;

public class LizPropertySource extends EnumerablePropertySource<LizConfigService> {

    public LizPropertySource(String name, LizConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
