package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.lang.reflect.Modifier;
import java.util.*;

public interface CollectionSerializer<T, V> extends Serializer<T, V> {

    /*static <T, V> CollectionSerializer<T, V> collection(Field field, Class<? extends T> inputClass) {
        if (Map.class.isAssignableFrom(inputClass)) {
            return (CollectionSerializer<T, V>) new FieldMapExternalizer(field, inputClass);
        }
        if (Collection.class.isAssignableFrom(inputClass)) {
            Class<?> genericClass = FieldSerializer.getGenericClass(field, 0);
            if (Long.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionLongExternalizer(field, inputClass);
            }
            if (Integer.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionIntegerExternalizer(field, inputClass);
            }
            if (Short.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionShortExternalizer(field, inputClass);
            }
            if (Double.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionDoubleExternalizer(field, inputClass);
            }
            if (Float.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionFloatExternalizer(field, inputClass);
            }
            if (Character.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionCharacterExternalizer(field, inputClass);
            }
            if (Byte.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionByteExternalizer(field, inputClass);
            }
            if (Boolean.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionBooleanExternalizer(field, inputClass);
            }
            return (CollectionSerializer<T, V>) new FieldCollectionExternalizer(field, inputClass);
        }
        return null;
    }*/

    static Class<?> collectionClass(final Class<?> inputClass) {
        if (!Modifier.isAbstract(inputClass.getModifiers()))
            return inputClass;
        if (Set.class.isAssignableFrom(inputClass))
            return LinkedHashSet.class;
        if (List.class.isAssignableFrom(inputClass))
            return ArrayList.class;
        if (Map.class.isAssignableFrom(inputClass))
            return LinkedHashMap.class;
        throw new SerializeException("Коллекция не поддерживается: " + inputClass);
    }


}
