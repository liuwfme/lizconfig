package cn.liz.lizconfig.server.controller;

import cn.liz.lizconfig.server.DistributedLocks;
import cn.liz.lizconfig.server.mapper.ConfigsMapper;
import cn.liz.lizconfig.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LizConfigController {

    @Autowired
    private DistributedLocks locks;

    @Autowired
    private ConfigsMapper configsMapper;

    Map<String, Long> VERSIONS = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> list(String app, String env, String namespace) {
        return configsMapper.list(app, env, namespace);
    }

    @RequestMapping("/update")
    public List<Configs> update(@RequestParam("app") String app, String env, String namespace,
                                @RequestBody Map<String, String> params) {
        params.forEach((k, v) -> {
            insertOrUpdate(new Configs(app, env, namespace, k, v));
        });
        VERSIONS.put(app + "-" + env + "-" + namespace, System.currentTimeMillis());
        return configsMapper.list(app, env, namespace);
    }

    private void insertOrUpdate(Configs configs) {
        Configs conf = configsMapper.select(configs.getApp(), configs.getEnv(), configs.getNamespace(), configs.getPkey());
        if (conf == null) {
            configsMapper.insert(configs);
        } else {
            configsMapper.update(configs);
        }
    }

    @GetMapping("/version")
    public long version(@RequestParam("app") String app, String env, String namespace) {
        return VERSIONS.getOrDefault(app + "-" + env + "-" + namespace, -1L);
    }

    @GetMapping("/status")
    public boolean status() {
        return locks.getLocked().get();
    }

}
