package cn.liz.lizconfig.client.config.spring;

import cn.liz.lizconfig.client.config.LizConfigService;
import cn.liz.lizconfig.client.config.LizConfigServiceImpl;
import cn.liz.lizconfig.client.config.spring.LizPropertySource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

    private static final String LIZ_PROPERTY_SOURCE = "lizPropertySource";

    private static final String LIZ_PROPERTY_SOURCES = "lizPropertySources";

    Environment environment;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if (env.getPropertySources().contains(LIZ_PROPERTY_SOURCES)) {
            return;
        }

        // 去config-server 远程获取配置
        // TODO: 2024/5/3
        Map<String, String> config = new HashMap<String, String>();
        config.put("liz.a", "dev300");
        config.put("liz.b", "dev400");
        config.put("liz.c", "dev500");

        LizConfigService configService = new LizConfigServiceImpl(config);

        LizPropertySource propertySource = new LizPropertySource(LIZ_PROPERTY_SOURCE, configService);

        CompositePropertySource composite = new CompositePropertySource(LIZ_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);

        env.getPropertySources().addFirst(composite);
    }

    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
