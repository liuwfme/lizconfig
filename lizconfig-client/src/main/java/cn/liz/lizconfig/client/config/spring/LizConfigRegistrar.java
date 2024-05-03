package cn.liz.lizconfig.client.config.spring;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

public class LizConfigRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println(" ====== register PropertySourcesProcessor");

        String beanDefinitionName = PropertySourcesProcessor.class.getName();

        Optional<String> any = Arrays.stream(registry.getBeanDefinitionNames())
                .filter(beanDefinitionName::equals)
                .findAny();

        if (any.isPresent()) {
            System.out.println("PropertySourcesProcessor already registered !");
            return;
        }

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition(beanDefinitionName, beanDefinition);

    }
}
