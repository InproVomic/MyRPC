package com.cbb.example.consumer;

import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.util.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
