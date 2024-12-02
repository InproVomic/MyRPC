package com.cbb.MyRPC;

import com.cbb.MyRPC.config.RegistryConfig;
import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.constant.RpcConstant;
import com.cbb.MyRPC.registry.Registry;
import com.cbb.MyRPC.registry.RegistryFactory;
import com.cbb.MyRPC.util.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newrpcConfig) {
        rpcConfig = newrpcConfig;
        log.info("rpc init, config: {}", rpcConfig.toString());
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("rpc register init, config: {}", registryConfig.toString());
        // JVM关闭时，执行销毁操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    public static void init(){
        RpcConfig config = null;
        try{
            config = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
            String url = config.getRegistryConfig().getAddress();
            if(!url.startsWith("http://") && !url.startsWith("https://")){
                config.getRegistryConfig().setAddress("http://" + url);
            }
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
