package cn.liz.lizconfig.client.repository;

import cn.liz.lizconfig.client.config.ConfigMeta;

import java.util.Map;

public interface ChangeListener {
    void onChange(ChangeEvent event);

    record ChangeEvent(ConfigMeta configMeta, Map<String, String> config) {
    }
}
