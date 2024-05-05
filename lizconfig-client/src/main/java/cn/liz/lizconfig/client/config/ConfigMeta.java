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

    public String getKey() {
        return this.getApp() + "_" + this.getEnv() + "_" + this.getNamespace();
    }

    public String listPath() {
        return path("list");
    }

    public String versionPath() {
        return path("version");
    }

    private String path(String context) {
        return this.getConfigServer() + "/" + context + "?app=" + this.getApp()
                + "&env=" + this.getEnv() + "&namespace=" + this.getNamespace();
    }
}
