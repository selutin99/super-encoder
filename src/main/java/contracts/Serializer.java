package contracts;

import service.serializers.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;

public interface Serializer<T, V> {

    static <T, V> Serializer<T, V> of(Class<? extends T> inputClass) {
        Serializer<T, V> serializer;
        if ((serializer = LangSerializer.lang(inputClass)) != null) {
            return serializer;
        }
        if ((serializer = TimeSerializer.time(inputClass)) != null) {
            return serializer;
        }
        return (Serializer<T, V>) ClassSerializer.of(inputClass);
    }

    static <T, V> Serializer<T, V> of(Field field, Class<? extends T> inputClass) {
        Serializer<T, V> serializer;
        if (inputClass.isPrimitive()) {
            if ((serializer = (Serializer<T, V>) PrimitiveSerializer.primitive(field, inputClass)) != null) {
                return serializer;
            }
        }
        /*if ((serializer = (Serializer<T, V>) CollectionExternalizer.collection(field, inputClass)) != null){
            return serializer;
        }*/
        if ((serializer = (Serializer<T, V>) LangSerializer.lang(field, inputClass)) != null) {
            return serializer;
        }
        if ((serializer = (Serializer<T, V>) TimeSerializer.time(field, inputClass)) != null) {
            return serializer;
        }
        return new FieldSerializer.FieldParentSerializer(field, ClassSerializer.of(inputClass));
    }

    void writeSerializer(T object, ObjectOutput out) throws IOException, ReflectiveOperationException;

    void readSerializer(final T object, final ObjectInput in) throws IOException, ReflectiveOperationException;

    V readObject(ObjectInput in) throws IOException, ReflectiveOperationException;
}
