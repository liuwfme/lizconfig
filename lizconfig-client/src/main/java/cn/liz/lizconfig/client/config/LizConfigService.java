package cn.liz.lizconfig.client.config;

public interface LizConfigService {

    String[] getPropertyNames();

    String getProperty(String name);
}
