package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;

public interface PrimitiveSerializer<T, V> extends Serializer<T, V> {

    static <T, V> PrimitiveSerializer<T, V> primitive(Field field, Class<T> inputClass) {
        if (Integer.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldIntegerSerializer(field);
        if (Short.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldShortSerializer(field);
        if (Long.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldLongSerializer(field);
        if (Float.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldFloatSerializer(field);
        if (Double.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldDoubleSerializer(field);
        if (Boolean.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldBooleanSerializer(field);
        if (Byte.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldByteSerializer(field);
        if (Character.TYPE.equals(inputClass))
            return (PrimitiveSerializer<T, V>) new FieldCharSerializer(field);
        return null;
    }

    abstract class FieldPrimitiveSerializer<T, V> extends FieldSerializer<T, V>
            implements PrimitiveSerializer<T, V> {
        protected FieldPrimitiveSerializer(Field field) {
            super(field);
        }

        @Override
        final public V readObject(ObjectInput in) throws IOException {
            throw new SerializeException("Не примитивный тип");
        }
    }

    final class FieldIntegerSerializer<T> extends FieldPrimitiveSerializer<T, Integer> {

        private FieldIntegerSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeInt(field.getInt(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setInt(object, in.readInt());
        }

    }

    final class FieldLongSerializer<T> extends FieldPrimitiveSerializer<T, Long> {

        private FieldLongSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeLong(field.getLong(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setLong(object, in.readLong());
        }

    }

    final class FieldShortSerializer<T> extends FieldPrimitiveSerializer<T, Short> {

        private FieldShortSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeShort(field.getShort(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setShort(object, in.readShort());
        }

    }

    final class FieldFloatSerializer<T> extends FieldPrimitiveSerializer<T, Float> {

        private FieldFloatSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeFloat(field.getFloat(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setFloat(object, in.readFloat());
        }

    }

    final class FieldDoubleSerializer<T> extends FieldPrimitiveSerializer<T, Double> {

        private FieldDoubleSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeDouble(field.getDouble(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setDouble(object, in.readDouble());
        }

    }

    final class FieldByteSerializer<T> extends FieldPrimitiveSerializer<T, Byte> {

        private FieldByteSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeByte(field.getByte(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setByte(object, in.readByte());
        }

    }

    final class FieldCharSerializer<T> extends FieldPrimitiveSerializer<T, Character> {

        private FieldCharSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeChar(field.getChar(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setChar(object, in.readChar());
        }

    }

    final class FieldBooleanSerializer<T> extends FieldPrimitiveSerializer<T, Boolean> {

        private FieldBooleanSerializer(Field field) {
            super(field);
        }

        @Override
        final public void writeSerializer(T object, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            out.writeBoolean(field.getBoolean(object));
        }

        @Override
        final public void readSerializer(T object, ObjectInput in)
                throws IOException, ReflectiveOperationException {
            field.setBoolean(object, in.readBoolean());
        }

    }
}
