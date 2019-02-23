package service;

import contracts.Serializer;
import contracts.SuperEncoder;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
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
            try (final GZIPOutputStream compressed = new GZIPOutputStream(output)) {
                try (final ObjectOutputStream objected = new ObjectOutputStream(compressed)) {
                    serializer.writeSerializer(anyBean, objected);
                    return output.toByteArray();
                }
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } else {
            throw new NullPointerException();
        }
        return null;
    }


    public Object deserialize(byte[] data) {
        if (data != null) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 ObjectInput in = new ObjectInputStream(bis)) {
                final Serializer serializer = of(in.readObject().getClass());
                try (final GZIPInputStream compressed = new GZIPInputStream(bis)) {
                    try (final ObjectInputStream objected = new ObjectInputStream(compressed)) {
                        return serializer.readObject(objected);
                    }
                }
            } catch (IOException | ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } else {
            throw new NullPointerException();
        }
        return null;
    }
}
