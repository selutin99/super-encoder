package service;

import contracts.Serializer;
import contracts.SuperEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPOutputStream;

public class SerializeService implements SuperEncoder {

    private final static ConcurrentHashMap<Class<?>, Serializer> externalizerMap = new ConcurrentHashMap();

    public final static <T> Serializer<T, T> of(Class<T> inputClass) {
        return externalizerMap.computeIfAbsent(inputClass, aClass -> Serializer.of(aClass));
    }

    public byte[] serialize(Object anyBean) {
        if (anyBean != null) {
            Serializer serializer = of(anyBean.getClass());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (GZIPOutputStream compressed = new GZIPOutputStream(output)) {
                try (ObjectOutputStream objected = new ObjectOutputStream(compressed)) {
                    serializer.writeSerializer(anyBean, objected);
                    return output.toByteArray();
                }
            }
            catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } else {
            throw new NullPointerException();
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] data, int a) {
        return null;
    }
}
