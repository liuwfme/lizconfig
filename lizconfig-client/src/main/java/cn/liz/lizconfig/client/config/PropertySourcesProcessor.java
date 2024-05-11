package cn.liz.lizconfig.client.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

public class PropertySourcesProcessor implements BeanFactoryPostProcessor,
        ApplicationContextAware, EnvironmentAware, PriorityOrdered {

    private static final String LIZ_PROPERTY_SOURCE = "lizPropertySource";
    private static final String LIZ_PROPERTY_SOURCES = "lizPropertySources";

    Environment environment;
    ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment environment = (ConfigurableEnvironment) this.environment;
        if (environment.getPropertySources().contains(LIZ_PROPERTY_SOURCES)) {
            return;
        }

        // 去config-server 远程获取配置
        String app = environment.getProperty("lizconfig.app", "app1");
        String env = environment.getProperty("lizconfig.env", "dev");
        String namespace = environment.getProperty("lizconfig.namespace", "public");
        String configServer = environment.getProperty("lizconfig.configServer", "http://localhost:9129");

        ConfigMeta configMeta = new ConfigMeta(app, env, namespace, configServer);
        LizConfigService configService = LizConfigService.getDefault(applicationContext, configMeta);
        LizPropertySource propertySource = new LizPropertySource(LIZ_PROPERTY_SOURCE, configService);
        CompositePropertySource composite = new CompositePropertySource(LIZ_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);

        environment.getPropertySources().addFirst(composite);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
