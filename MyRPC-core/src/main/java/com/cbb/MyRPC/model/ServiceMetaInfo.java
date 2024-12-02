package com.cbb.MyRPC.model;

import cn.hutool.core.util.StrUtil;
import com.cbb.MyRPC.constant.RpcConstant;
import lombok.Data;


/**
 * 服务元信息
 */
@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String version = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 服务地址
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务分组(暂未实现）
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     * @return String.format("%s:%s", serviceName,version);
     */
    public String getServiceKey() {
        return String.format("%s:%s", serviceName,version);
    }

    /**
     * 获取服务注册节点键名
     * @return String.format("%s/%s:%d", getServiceKey(),serviceHost,servicePort);
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%d", getServiceKey(),serviceHost,servicePort);
    }

    /**
     * 获取完整的服务地址
     */
    public String getServiceAddress() {
        if(!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%d", serviceHost,servicePort);
        }
        return String.format("%s:%d", serviceHost,servicePort);
    }
}
