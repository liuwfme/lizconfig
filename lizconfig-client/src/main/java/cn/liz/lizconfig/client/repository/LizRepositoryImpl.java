package cn.liz.lizconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import cn.liz.lizconfig.client.config.ConfigMeta;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LizRepositoryImpl implements LizRepository {

    private final ConfigMeta configMeta;

    private final Map<String, Long> versionMap = new HashMap<>();
    private final Map<String, Map<String, String>> configMap = new HashMap<>();

    private final List<ChangeListener> listeners = new ArrayList<>();

    public LizRepositoryImpl(ConfigMeta configMeta) {
        this.configMeta = configMeta;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(this::heartbeat, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = configMeta.getKey();
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }
        return findAll();
    }

    private Map<String, String> findAll() {
        String listPath = configMeta.listPath();
        log.info(" ====== getConfig from server, url : {}", listPath);
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {
        });
        log.info(" ====== getConfig from server resp configs : {}", configs);
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(config -> resultMap.put(config.getPkey(), config.getPval()));
        return resultMap;
    }

    private void heartbeat() {
        String versionPath = configMeta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, Long.class);

        String key = configMeta.getKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if (version > oldVersion) {
            log.info(" ====== need update configs!!! version:{}, oldVersion:{}", version, oldVersion);
            versionMap.put(key, version);
            Map<String, String> newConfigs = findAll();
            configMap.put(key, newConfigs);
            listeners.forEach(listener -> listener.onChange(new ChangeListener.ChangeEvent(configMeta, newConfigs)));
        }
    }
}
