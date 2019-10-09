package the.flash.serialize;

import the.flash.serialize.impl.JSONSerializer;

public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     *
     * @return
     */
    byte getSerializerAlgrithm();

    /**
     * java 对象转成二进制
     *
     * @param object
     * @return
     */
    byte[] serilize(Object object);

    /**
     * 二进制转成java对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserializer(Class<T> clazz, byte[] bytes);

}
