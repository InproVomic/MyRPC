package com.cbb.MyRPC.config;

import com.cbb.MyRPC.registry.RegistryKeys;
import lombok.Data;

@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间(单位：毫秒)
     */
    private long timeout = 10000L;
}