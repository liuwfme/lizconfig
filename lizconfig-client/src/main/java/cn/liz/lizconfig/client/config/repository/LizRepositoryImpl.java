package cn.liz.lizconfig.client.config.repository;

import cn.kimmking.utils.HttpUtils;
import cn.liz.lizconfig.client.config.ConfigMeta;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class LizRepositoryImpl implements LizRepository {

    ConfigMeta configMeta;

    @Override
    public Map<String, String> getConfig() {
        String listPath = configMeta.getConfigServer() + "/list?app=" + configMeta.getApp()
                + "&env=" + configMeta.getEnv() + "&namespace=" + configMeta.getNamespace();
        log.info(" ====== getConfig from server, url : {}", listPath);
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {
        });
        log.info(" ====== getConfig from server resp configs : {}", configs);
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(config -> resultMap.put(config.getPkey(), config.getPval()));
        return resultMap;
    }
}
