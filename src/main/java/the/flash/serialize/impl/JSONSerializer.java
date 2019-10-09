package the.flash.serialize.impl;

import com.alibaba.fastjson.JSON;
import the.flash.serialize.Serializer;
import the.flash.serialize.SerializerAlgorithm;

public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlgrithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serilize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserializer(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
