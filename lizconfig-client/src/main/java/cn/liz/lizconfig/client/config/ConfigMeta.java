package cn.liz.lizconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMeta {
    private String app;
    private String env;
    private String namespace;
    private String configServer;
}
