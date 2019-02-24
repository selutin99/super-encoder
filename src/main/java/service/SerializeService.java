package service;

import contracts.Serializer;
import contracts.SuperEncoder;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class SerializeService implements SuperEncoder {

    private static ConcurrentHashMap<Class<?>, Serializer> externalizerMap = new ConcurrentHashMap();

    public static <T> Serializer<T, T> of(Class<T> inputClass) {
        return externalizerMap.computeIfAbsent(inputClass, aClass -> Serializer.of(aClass));
    }

    public byte[] serialize(Object anyBean) {
        if (anyBean != null) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Serializer serializer = of(anyBean.getClass());

            try (ObjectOutputStream objected = new ObjectOutputStream(output)) {
                serializer.writeSerializer(anyBean, objected);
                return output.toByteArray();
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
    public Object deserialize(byte[] data) {
        if (data != null) {
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            try {
                ObjectInputStream is = new ObjectInputStream(input);
                Serializer serializer = of(is.readObject().getClass());
                return serializer.readObject(is);
            }
            catch(IOException | ReflectiveOperationException e){
                e.printStackTrace();
            }
        }
        else{
            throw new NullPointerException();
        }
        return null;
    }

    public static final void serializeRaw(final Object object, final OutputStream output) throws IOException, ReflectiveOperationException {
        final Serializer serializer = of(object.getClass());
        try (final ObjectOutputStream objected = new ObjectOutputStream(output)) {
            serializer.writeSerializer(object, objected);
        }
    }

    public static final <T> T deserializeRaw(final InputStream input, final Class<T> clazz)
            throws IOException, ReflectiveOperationException {
        final Serializer<T, T> serializer = of(clazz);
        try (final ObjectInputStream objected = new ObjectInputStream(input)) {
            return serializer.readObject(objected);
        }
    }
}
