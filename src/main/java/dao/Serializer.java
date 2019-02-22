package dao;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;

public interface Serializer<T, V> {

    static <T, V> Serializer<T, V> of(Class<? extends T> inputClass) {
        Serializer<T, V> serializer;
        if ((serializer = LangExternalizer.lang(inputClass)) != null)
            return serializer;
        if ((serializer = TimeExternalizer.time(inputClass)) != null)
            return serializer;
        return (Serializer<T, V>) ClassExternalizer.of(inputClass);
    }

    static <T, V> Serializer<T, V> of(Field field, Class<? extends T> inputClass) {
        Serializer<T, V> serializer;
        if (inputClass.isPrimitive())
            if ((serializer = (Serializer<T, V>) PrimitiveExternalizer.primitive(field, inputClass)) != null)
                return serializer;
        if (inputClass.isArray())
            if ((serializer = (Serializer<T, V>) ArrayExternalizer.array(field, inputClass)) != null)
                return serializer;
        if ((serializer = (Serializer<T, V>) CollectionExternalizer.collection(field, inputClass)) != null)
            return serializer;
        if ((serializer = (Serializer<T, V>) LangExternalizer.lang(field, inputClass)) != null)
            return serializer;
        if ((serializer = (Serializer<T, V>) TimeExternalizer.time(field, inputClass)) != null)
            return serializer;
        return new FieldExternalizer.FieldParentExternalizer(field, ClassExternalizer.of(inputClass));
    }

    void writeObject(T object, ObjectOutput out) throws IOException;

    V readObject(ObjectInput in) throws IOException;
}
