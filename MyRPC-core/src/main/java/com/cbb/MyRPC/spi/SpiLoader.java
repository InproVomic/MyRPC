package com.cbb.MyRPC.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.cbb.MyRPC.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {
    /**
     * 存储已经加载的类：接口名=>(key=>实现类)
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /**
     * 对象缓存实例
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统路径
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户路径
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIR = {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载类实例
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有的类实例
     */
    public static void loadAll() {
        log.info("加载所有的类实例");
        for (Class<?> aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    public static Map<String,Class<?>> load(Class<?> aClass) {
        log.info("加载类实例:{}", aClass.getName());
        Map<String, Class<?>> map = new HashMap<>();
        // 扫描系统路径和用户路径，用户路径的优先级更高
        for(String dir : SCAN_DIR) {
            // 获取资源列表
            List<URL> resources = ResourceUtil.getResources(dir + aClass.getName());
            // 读取每个资源文件
            for(URL url : resources) {
                try {
                    InputStreamReader reader = new InputStreamReader(url.openStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        String[] strArray = line.split("=");
                        if(strArray.length > 1){
                            String key = strArray[0];
                            String className = strArray[1];
                            map.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("加载类实例失败:{}", aClass.getName(), e);
                }
            }
        }
        loaderMap.put(aClass.getName(), map);
        return map;
    }

    public static <T> T getInstance(Class<T> tClass,String key) {
        String className = tClass.getName();
        log.info("获取类实例:{}", tClass.getName());
        Map<String, Class<?>> keyClassMap = loaderMap.get(className);
        if(keyClassMap == null){
            throw new RuntimeException("类实例未加载: " + className);
        }
        if(!keyClassMap.containsKey(key)){
            throw new RuntimeException("类实例未加载: " + key);
        }

        Class<?> aClass = keyClassMap.get(key);
        String keyClassName = aClass.getName();
        if(!instanceCache.containsKey(keyClassName)){
            try {
                instanceCache.put(keyClassName, aClass.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                String msg = "类实例化失败: " + keyClassName;
                throw new RuntimeException(msg,e);
            }
        }
        return (T) instanceCache.get(keyClassName);
    }
}
