package service;

import contracts.Serializer;
import contracts.SuperEncoder;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class SerializeService implements SuperEncoder {

    private Class inputClass;
    private static ConcurrentHashMap<Class<?>, Serializer> externalizerMap = new ConcurrentHashMap();

    public SerializeService(Class inputClass){
        this.inputClass = inputClass;
    }

    public Class getInputClass() {
        return inputClass;
    }

    public void setInputClass(Class inputClass) {
        this.inputClass = inputClass;
    }

    private static <T> Serializer<T, T> of(Class<T> inputClass) {
        return externalizerMap.computeIfAbsent(inputClass, aClass -> Serializer.of(aClass));
    }

    @Override
    public byte[] serialize(Object anyBean) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            this.serialize(anyBean, output);
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] data) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
            return this.deserialize(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void serialize(Object object, OutputStream output) {
        Serializer serializer = of(object.getClass());
        try {
            try (ObjectOutputStream objected = new ObjectOutputStream(output)) {
                serializer.writeSerializer(object, objected);
            }
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private <T> T deserialize(InputStream input) {
        Serializer<T, T> serializer = of(getInputClass());
        try {
            try (ObjectInputStream objected = new ObjectInputStream(input)) {
                return serializer.readObject(objected);
            }
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
