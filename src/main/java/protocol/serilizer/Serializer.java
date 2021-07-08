package protocol.serilizer;

import com.alibaba.fastjson.JSON;

public interface Serializer {

    byte[] serialize(Object object);

    <T> T deSerialize(byte[] bytes, Class<T> clazz);

    enum Algorithm implements Serializer {

        FASTJSON {

            @Override
            public byte[] serialize(Object object) {
                return JSON.toJSONBytes(object);
            }

            @Override
            public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
                return JSON.parseObject(bytes, clazz);
            }
        }
    }
}