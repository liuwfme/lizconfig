package cn.liz.lizconfig.client.annotation;

import cn.liz.lizconfig.client.config.spring.LizConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({LizConfigRegistrar.class})
public @interface EnableLizConfig {
}
