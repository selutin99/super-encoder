package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public abstract class FieldSerializer<T, V> implements Serializer<T, V> {

    protected Field field;

    protected FieldSerializer(Field field) {
        this.field = field;
    }

    final static Class<?> getGenericClass(Field field, int pos) {
        ParameterizedType paramTypes = (ParameterizedType) field.getGenericType();
        return (Class<?>) paramTypes.getActualTypeArguments()[pos];
    }

    public static abstract class FieldObjectSerializer<T, V> extends FieldSerializer<T, V> {

        protected FieldObjectSerializer(Field field) {
            super(field);
        }

        protected abstract void writeValue(V value, ObjectOutput out) throws IOException, ReflectiveOperationException;

        @Override
        public void readSerializer(T object, ObjectInput in) throws IOException, ReflectiveOperationException {
            field.set(object, readObject(in));
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out) throws IOException, ReflectiveOperationException {
            V value = (V) field.get(object);
            if (value == null) {
                out.writeBoolean(false);
                return;
            }
            out.writeBoolean(true);
            writeValue(value, out);
        }
    }

    public static class FieldParentSerializer<T, V> extends FieldObjectSerializer<T, V> {

        private Serializer<V, V> serializer;

        public FieldParentSerializer(Field field, Serializer<V, V> serializer) {
            super(field);
            this.serializer = serializer;
        }

        @Override
        final public V readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            return in.readBoolean() ? serializer.readObject(in) : null;
        }

        @Override
        final protected void writeValue(V value, ObjectOutput out) throws IOException, ReflectiveOperationException {
            serializer.writeSerializer(value, out);
        }
    }

    public static abstract class FieldConstructorSerializer<T, C> extends FieldObjectSerializer<T, C> {

        protected Constructor<? extends C> constructor;

        protected FieldConstructorSerializer(Field field, Class<? extends C> inputClass) {
            super(field);
            try {
                constructor = inputClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new SerializeException("Нет конструктора по умолчанию для поля " + inputClass);
            }
        }

        protected Serializer<Object, ?> getGeneric(int pos) {
            return Serializer.of(getGenericClass(field, pos));
        }
    }

}
