package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;

public interface LangSerializer<T, V> extends Serializer<T, V> {

    static <T, V> LangSerializer<T, V> lang(Class<? extends T> inputClass) {
        if (String.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) StringSerializer.INSTANCE;
        }
        if (Long.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) LongSerializer.INSTANCE;
        }
        if (Integer.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) IntegerSerializer.INSTANCE;
        }
        if (Short.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) ShortSerializer.INSTANCE;
        }
        if (Double.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) DoubleSerializer.INSTANCE;
        }
        if (Float.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) FloatSerializer.INSTANCE;
        }
        if (Character.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) CharSerializer.INSTANCE;
        }
        if (Byte.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) ByteSerializer.INSTANCE;
        }
        if (Boolean.class.isAssignableFrom(inputClass)) {
            return (LangSerializer<T, V>) BooleanSerializer.INSTANCE;
        }
        return null;
    }

    static <T, V> FieldSerializer<T, V> lang(Field field, Class<? extends T> inputClass) {
        if (String.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, StringSerializer.INSTANCE);
        }
        if (Long.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, LongSerializer.INSTANCE);
        }
        if (Integer.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, IntegerSerializer.INSTANCE);
        }
        if (Short.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, ShortSerializer.INSTANCE);
        }
        if (Double.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, DoubleSerializer.INSTANCE);
        }
        if (Float.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, FloatSerializer.INSTANCE);
        }
        if (Character.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, CharSerializer.INSTANCE);
        }
        if (Byte.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, ByteSerializer.INSTANCE);
        }
        if (Boolean.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, BooleanSerializer.INSTANCE);
        }
        return null;
    }

    @Override
    default void readSerializer(T object, ObjectInput in) throws IOException, ReflectiveOperationException {
        throw new SerializeException("Не поддерживается!");
    }

    class StringSerializer implements LangSerializer<String, String> {

        static StringSerializer INSTANCE = new StringSerializer();

        @Override
        public void writeSerializer(String object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeUTF(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public String readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readUTF() : null;
        }
    }

    class LongSerializer implements LangSerializer<Long, Long> {

        static LongSerializer INSTANCE = new LongSerializer();

        @Override
        public void writeSerializer(Long object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeLong(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Long readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readLong() : null;
        }
    }

    class IntegerSerializer implements LangSerializer<Integer, Integer> {

        static IntegerSerializer INSTANCE = new IntegerSerializer();

        @Override
        public void writeSerializer(Integer object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeInt(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Integer readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readInt() : null;
        }
    }

    class ShortSerializer implements LangSerializer<Short, Short> {

        static ShortSerializer INSTANCE = new ShortSerializer();

        @Override
        public void writeSerializer(Short object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeShort(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Short readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readShort() : null;
        }
    }

    class DoubleSerializer implements LangSerializer<Double, Double> {

        static DoubleSerializer INSTANCE = new DoubleSerializer();

        @Override
        public void writeSerializer(Double object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeDouble(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Double readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readDouble() : null;
        }
    }

    class FloatSerializer implements LangSerializer<Float, Float> {

        static FloatSerializer INSTANCE = new FloatSerializer();

        @Override
        public void writeSerializer(Float object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeFloat(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Float readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readFloat() : null;
        }
    }

    class ByteSerializer implements LangSerializer<Byte, Byte> {

        static ByteSerializer INSTANCE = new ByteSerializer();

        @Override
        public void writeSerializer(Byte object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeByte(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Byte readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readByte() : null;
        }
    }

    class CharSerializer implements LangSerializer<Character, Character> {

        static CharSerializer INSTANCE = new CharSerializer();

        @Override
        public void writeSerializer(Character object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeChar(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Character readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readChar() : null;
        }
    }

    class BooleanSerializer implements LangSerializer<Boolean, Boolean> {

        static BooleanSerializer INSTANCE = new BooleanSerializer();

        @Override
        public void writeSerializer(Boolean object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeBoolean(object);
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Boolean readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readBoolean() : null;
        }
    }
}
