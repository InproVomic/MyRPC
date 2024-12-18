package com.cbb.MyRPC.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {
    //加载配置对象，不区分环境
    public static <T> T loadConfig(Class<T> tClass,String prefix) {
        return loadConfig(tClass,prefix,"");
    }

    //加载配置对象，支持区分环境
    public static <T> T loadConfig(Class<T> tClass,String prefix,String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass,prefix);
    }
}
