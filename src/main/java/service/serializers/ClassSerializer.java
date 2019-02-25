package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

public interface ClassSerializer<T> extends Serializer<T, T> {

    static <T> ClassSerializer<T> of(Class<T> inputClass) {
        try {
            Constructor<T> constructor = inputClass.getConstructor();
            Collection<Serializer> serializers = new ArrayList<>();
            detectFields(inputClass, serializers);
            if (serializers.size() > 0) {
                return new RootSerializer(constructor, serializers);
            }
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (Serializable.class.isAssignableFrom(inputClass)) {
            return new SerializableSerializer<>(inputClass);
        }
        throw new SerializeException("Нужен конструктор по умолчанию. Не могу сеарилизовать: " + inputClass);
    }

    static void checkCircularReference(Field field, Class<?> inputClass){
        Class<?> fieldClass = field.getType();
        Field[] fieldsOfField = fieldClass.getDeclaredFields();
        for(Field fieldOfField: fieldsOfField){
            if(fieldOfField.getType().getCanonicalName().equals(inputClass.getCanonicalName())){
                throw new SerializeException("Найдена циклическая ссылка");
            }
        }
    }

    static void detectFields(Class<?> inputClass, Collection<Serializer> serializers) {
        if (inputClass == null) {
            return;
        }
        Field[] fields = inputClass.getDeclaredFields();
        for (Field field : fields) {
            Class<?> cl = field.getType();
            int modifier = field.getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isTransient(modifier)) {
                continue;
            }
            if(!field.getType().isPrimitive()){
                checkCircularReference(field, inputClass);
            }
            field.setAccessible(true);
            Serializer fieldExt = Serializer.of(field, cl);
            serializers.add(fieldExt);
        }
        detectFields(inputClass.getSuperclass(), serializers);
    }

    class RootSerializer<T> implements ClassSerializer<T> {

        private Constructor<T> constructor;
        private Collection<Serializer> serializers;

        private RootSerializer(Constructor<T> constructor, Collection<Serializer> serializers) {
            this.constructor = constructor;
            this.serializers = serializers;
        }

        @Override
        public void writeSerializer(T object, ObjectOutput out) throws IOException, ReflectiveOperationException {
            for (Serializer serializer : serializers) {
                serializer.writeSerializer(object, out);
            }
        }

        @Override
        public void readSerializer(T object, ObjectInput in) throws IOException, ReflectiveOperationException {
            for (Serializer serializer : serializers) {
                serializer.readSerializer(object, in);
            }
        }

        @Override
        public T readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            T object = constructor.newInstance();
            readSerializer(object, in);
            return object;
        }
    }

    class SerializableSerializer<T> implements ClassSerializer<T> {

        private Class<T> inputClass;

        private SerializableSerializer(Class<T> clazz) {
            this.inputClass = clazz;
        }

        @Override
        public void writeSerializer(T object, ObjectOutput out) throws IOException, ReflectiveOperationException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeObject(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public void readSerializer(T object, ObjectInput in) throws IOException, ReflectiveOperationException {
            throw new SerializeException("Не могу прочитать из " + inputClass);
        }

        @Override
        public T readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            return in.readBoolean() ? (T) in.readObject() : null;
        }
    }
}
