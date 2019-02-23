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

    final class StringSerializer implements LangSerializer<String, String> {

        static final StringSerializer INSTANCE = new StringSerializer();

        @Override
        final public void writeSerializer(String object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeUTF(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public String readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readUTF() : null;
        }
    }

    final class LongSerializer implements LangSerializer<Long, Long> {

        static final LongSerializer INSTANCE = new LongSerializer();

        @Override
        final public void writeSerializer(Long object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeLong(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Long readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readLong() : null;
        }
    }

    final class IntegerSerializer implements LangSerializer<Integer, Integer> {

        static final IntegerSerializer INSTANCE = new IntegerSerializer();

        @Override
        final public void writeSerializer(Integer object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeInt(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Integer readObject(final ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readInt() : null;
        }
    }

    final class ShortSerializer implements LangSerializer<Short, Short> {

        static final ShortSerializer INSTANCE = new ShortSerializer();

        @Override
        final public void writeSerializer(Short object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeShort(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Short readObject(final ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readShort() : null;
        }
    }

    final class DoubleSerializer implements LangSerializer<Double, Double> {

        static final DoubleSerializer INSTANCE = new DoubleSerializer();

        @Override
        final public void writeSerializer(Double object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeDouble(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Double readObject(final ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readDouble() : null;
        }
    }

    final class FloatSerializer implements LangSerializer<Float, Float> {

        static final FloatSerializer INSTANCE = new FloatSerializer();

        @Override
        final public void writeSerializer(Float object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeFloat(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Float readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readFloat() : null;
        }
    }

    final class ByteSerializer implements LangSerializer<Byte, Byte> {

        static final ByteSerializer INSTANCE = new ByteSerializer();

        @Override
        final public void writeSerializer(Byte object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeByte(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Byte readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readByte() : null;
        }
    }

    final class CharSerializer implements LangSerializer<Character, Character> {

        static final CharSerializer INSTANCE = new CharSerializer();

        @Override
        final public void writeSerializer(Character object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeChar(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Character readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readChar() : null;
        }
    }

    final class BooleanSerializer implements LangSerializer<Boolean, Boolean> {

        static final BooleanSerializer INSTANCE = new BooleanSerializer();

        @Override
        final public void writeSerializer(Boolean object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeBoolean(object);
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        final public Boolean readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? in.readBoolean() : null;
        }
    }
}
