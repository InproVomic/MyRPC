package com.cbb.MyRPC.serializer;

import java.io.*;

public class JdkSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        }catch (ClassNotFoundException e) {
            throw new RuntimeException("反序列化失败",e);
        }finally {
            objectInputStream.close();
        }
    }
}
