package cn.liz.lizconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import cn.liz.lizconfig.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 1.扫描所有的 spring value，保存起来
 * 2.在配置变更时，更新所有的 spring value
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    @Setter
    private BeanFactory beanFactory;

    static final PlaceholderHelper helper = PlaceholderHelper.getInstance();

    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(field -> {
            log.info("====== spring value field : {}", field);
            Value value = field.getAnnotation(Value.class);
            log.info("====== spring value.value : {}", value);
            helper.extractPlaceholderKeys(value.value()).forEach(key -> {
                log.info("====== spring value key : {}", key);
                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                VALUE_HOLDER.add(key, springValue);
            });
        });
        return bean;
    }

    //    @EventListener
//    public void onChange(EnvironmentChangeEvent event) { 与下面的写法等价
//    }
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("====== update spring value keys : {}", event.getKeys());
        event.getKeys().forEach(key -> {
            log.info("====== update spring value key : {}", key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if (springValues == null || springValues.isEmpty()) {
                return;
            }
            springValues.forEach(springValue -> {
                log.info("====== update spring value key : {}, springValue : {}", key, springValue);
                try {
                    Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,
                            springValue.getBeanName(), springValue.getPlaceholder());
                    log.info("====== update spring value springValue : {}, placeholder : {}",
                            springValue, springValue.getPlaceholder());

                    springValue.getField().setAccessible(true);
                    springValue.getField().set(springValue.getBean(), value);
                } catch (Exception e) {
                    log.error("update spring value err, e : ", e);
                }
            });
        });
    }

}
