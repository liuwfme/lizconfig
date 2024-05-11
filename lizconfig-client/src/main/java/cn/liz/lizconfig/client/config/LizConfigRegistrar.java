package cn.liz.lizconfig.client.config;

import cn.liz.lizconfig.client.value.SpringValueProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

public class LizConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println(" ====== register PropertySourcesProcessor");
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);

    }

    private void registerClass(BeanDefinitionRegistry registry, Class<?> clazz) {
        Optional<String> any = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(clazz.getName()::equals)
                .findAny();
        if (any.isPresent()) {
            System.out.println("PropertySourcesProcessor already registered !");
            return;
        }

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition();
        registry.registerBeanDefinition(clazz.getName(), beanDefinition);
    }
}
