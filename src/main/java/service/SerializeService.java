package service;

import dao.Serializer;
import dao.SuperEncoder;

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
        if(anyBean!=null){
            Serializer serializer = of(anyBean.getClass());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (final GZIPOutputStream compressed = new GZIPOutputStream(output)) {
                try (final ObjectOutputStream objected = new ObjectOutputStream(compressed)) {
                    serializer.writeObject(anyBean, objected);
                    return output.toByteArray();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            throw new NullPointerException();
        }
        return null;
    }

    public Object deserialize(byte[] data) {
        return null;
    }
}
