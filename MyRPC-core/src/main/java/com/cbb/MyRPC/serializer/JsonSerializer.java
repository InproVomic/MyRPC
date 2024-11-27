package com.cbb.MyRPC.serializer;

import com.cbb.MyRPC.model.RpcRequest;
import com.cbb.MyRPC.model.RpcResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonSerializer implements Serializer{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        T obj = OBJECT_MAPPER.readValue(data, clazz);
        if(obj instanceof RpcRequest){
            return handleRequest((RpcRequest) obj, clazz);
        }

        if(obj instanceof RpcResponse){
            return handleResponse((RpcResponse) obj, clazz);
        }
        return obj;
    }

    private <T> T handleRequest(RpcRequest request, Class<T> clazz) throws IOException {
        Class<?>[] paramTypes = request.getParameterTypes();
        Object[] args = request.getArgs();
        for(int i = 0; i < paramTypes.length; i++){
            Class<?> paramType = paramTypes[i];
            if(!paramType.isAssignableFrom(args[i].getClass())){
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(bytes, paramType);
            }
        }
        return clazz.cast(request);
    }

    private <T> T handleResponse(RpcResponse response, Class<T> clazz) throws IOException {
        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(response.getData());
        response.setData(OBJECT_MAPPER.readValue(bytes, response.getDataType()));
        return clazz.cast(response);
    }
}
