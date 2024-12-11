package com.cbb.MyRPC.loadbalancer;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.spi.SpiLoader;

public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    private final static LoadBalancer DEFAULT_LOADBALANCER = SpiLoader.getInstance(
            LoadBalancer.class, RpcApplication.getRpcConfig().getLoadBalancer());

    public static LoadBalancer getDefaultLoadbalancer() {
        return DEFAULT_LOADBALANCER;
    }

    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
