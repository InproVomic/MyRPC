package com.cbb.MyRPC;

import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.constant.RpcConstant;
import com.cbb.MyRPC.util.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newrpcConfig) {
        rpcConfig = newrpcConfig;
        log.info("rpc init, config: {}", rpcConfig.toString());
    }

    public static void init(){
        RpcConfig config = null;
        try{
            config = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            config = new RpcConfig();
        }
        init(config);
    }

    public static RpcConfig getRpcConfig() {
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
