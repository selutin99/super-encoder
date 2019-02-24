package service.serializers;

import contracts.Serializer;
import org.roaringbitmap.RoaringBitmap;
import org.xerial.snappy.Snappy;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public interface CollectionSerializer<T, V> extends Serializer<T, V> {

    static <T, V> CollectionSerializer<T, V> collection(Field field, Class<? extends T> inputClass) {
        if (Map.class.isAssignableFrom(inputClass)) {
            return (CollectionSerializer<T, V>) new FieldMapSerializer(field, inputClass);
        }
        if (Collection.class.isAssignableFrom(inputClass)) {
            Class<?> genericClass = FieldSerializer.getGenericClass(field, 0);
            if (Long.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionLongSerializer(field, inputClass);
            }
            if (Integer.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionIntegerSerializer(field, inputClass);
            }
            if (Short.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionShortSerializer(field, inputClass);
            }
            if (Double.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionDoubleSerializer(field, inputClass);
            }
            if (Float.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionFloatSerializer(field, inputClass);
            }
            if (Character.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionCharacterSerializer(field, inputClass);
            }
            if (Byte.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionByteSerializer(field, inputClass);
            }
            if (Boolean.class.isAssignableFrom(genericClass)) {
                return (CollectionSerializer<T, V>) new FieldCollectionBooleanSerializer(field, inputClass);
            }
            return (CollectionSerializer<T, V>) new FieldCollectionSerializer(field, inputClass);
        }
        return null;
    }

    static Class<?> collectionClass(Class<?> inputClass) {
        if (!Modifier.isAbstract(inputClass.getModifiers())) {
            return inputClass;
        }
        if (Set.class.isAssignableFrom(inputClass)) {
            return LinkedHashSet.class;
        }
        if (List.class.isAssignableFrom(inputClass)) {
            return ArrayList.class;
        }
        if (Map.class.isAssignableFrom(inputClass)) {
            return LinkedHashMap.class;
        }
        throw new SerializeException("Коллекция не поддерживается: " + inputClass);
    }

    abstract class FieldCollectionSnappySerializer<T, V>
            extends FieldSerializer.FieldConstructorSerializer<T, Collection<V>>
            implements CollectionSerializer<T, Collection<V>> {

        protected FieldCollectionSnappySerializer(Field field, Class<? extends Collection<V>> inputClass) {
            super(field, (Class<? extends Collection<V>>) collectionClass(inputClass));
        }

        protected interface NullableArray<V> {
            void set(int i, V value);
            byte[] compress() throws IOException;
        }

        protected abstract NullableArray getNullableArray(int size);

        @Override
        protected void writeValue(Collection<V> collection, ObjectOutput out)
                throws IOException, ReflectiveOperationException {
            NullableArray array = getNullableArray(collection.size());
            RoaringBitmap nullBitmap = new RoaringBitmap();
            int i = 0;
            for (V value : collection) {
                if (value == null) {
                    nullBitmap.add(i);
                }
                else {
                    array.set(i, value);
                }
                i++;
            }
            nullBitmap.writeExternal(out);

            out.writeInt(array.compress().length);
            out.write(array.compress());
        }

        abstract protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                               Collection<V> collection) throws IOException;

        @Override
        final public Collection<V> readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            if (!in.readBoolean()) {
                return null;
            }
            RoaringBitmap nullBitmap = new RoaringBitmap();
            nullBitmap.readExternal(in);
            Collection<V> collection = constructor.newInstance();
            fillCollection(in, nullBitmap, collection);
            return collection;
        }
    }

    final class FieldCollectionLongSerializer<T> extends FieldCollectionSnappySerializer<T, Long> {

        protected FieldCollectionLongSerializer(Field field, Class<? extends Collection<Long>> inputClass) {
            super(field, inputClass);
        }

        private class LongNullableArray implements NullableArray<Long> {

            long[] array;

            private LongNullableArray(int size) {
                array = new long[size];
            }

            @Override
            public void set(int i, Long value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new LongNullableArray(size);
        }

        protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                      Collection<Long> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            long[] array = Snappy.uncompressLongArray(bytes);
            int i = 0;
            for (long value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionIntegerSerializer<T> extends FieldCollectionSnappySerializer<T, Integer> {

        protected FieldCollectionIntegerSerializer(Field field, Class<? extends Collection<Integer>> inputClass) {
            super(field, inputClass);
        }

        private class IntegerNullableArray implements NullableArray<Integer> {

            int[] array;

            private IntegerNullableArray(int size) {
                array = new int[size];
            }

            @Override
            public void set(int i, Integer value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new IntegerNullableArray(size);
        }

        protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                      Collection<Integer> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            int[] array = Snappy.uncompressIntArray(bytes);
            int i = 0;
            for (int value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionShortSerializer<T> extends FieldCollectionSnappySerializer<T, Short> {

        protected FieldCollectionShortSerializer(Field field, Class<? extends Collection<Short>> inputClass) {
            super(field, inputClass);
        }

        private class ShortNullableArray implements NullableArray<Short> {

            short[] array;

            private ShortNullableArray(int size) {
                array = new short[size];
            }

            @Override
            public void set(int i, Short value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new ShortNullableArray(size);
        }

        protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                      Collection<Short> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            short[] array = Snappy.uncompressShortArray(bytes);
            int i = 0;
            for (short value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionDoubleSerializer<T> extends FieldCollectionSnappySerializer<T, Double> {

        protected FieldCollectionDoubleSerializer(Field field, Class<? extends Collection<Double>> inputClass) {
            super(field, inputClass);
        }

        private class DoubleNullableArray implements NullableArray<Double> {

            double[] array;

            private DoubleNullableArray(int size) {
                array = new double[size];
            }

            @Override
            public void set(int i, Double value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new DoubleNullableArray(size);
        }

        protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                      Collection<Double> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            double[] array = Snappy.uncompressDoubleArray(bytes);
            int i = 0;
            for (double value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionFloatSerializer<T> extends FieldCollectionSnappySerializer<T, Float> {

        protected FieldCollectionFloatSerializer(Field field, Class<? extends Collection<Float>> inputClass) {
            super(field, inputClass);
        }

        private class FloatNullableArray implements NullableArray<Float> {

            float[] array;

            private FloatNullableArray(int size) {
                array = new float[size];
            }

            @Override
            final public void set(int i, Float value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new FloatNullableArray(size);
        }

        protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                      Collection<Float> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            float[] array = Snappy.uncompressFloatArray(bytes);
            int i = 0;
            for (final float value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionCharacterSerializer<T> extends FieldCollectionSnappySerializer<T, Character> {

        protected FieldCollectionCharacterSerializer(Field field, Class<? extends Collection<Character>> inputClass) {
            super(field, inputClass);
        }

        private class CharacterNullableArray implements NullableArray<Character> {

            char[] array;

            private CharacterNullableArray(int size) {
                array = new char[size];
            }

            @Override
            final public void set(int i, Character value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new CharacterNullableArray(size);
        }

        final protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                            Collection<Character> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            char[] array = Snappy.uncompressCharArray(bytes);
            int i = 0;
            for (char value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionByteSerializer<T> extends FieldCollectionSnappySerializer<T, Byte> {

        protected FieldCollectionByteSerializer(Field field, Class<? extends Collection<Byte>> inputClass) {
            super(field, inputClass);
        }

        private class ByteNullableArray implements NullableArray<Byte> {

            byte[] array;

            private ByteNullableArray(int size) {
                array = new byte[size];
            }

            @Override
            public void set(int i, Byte value) {
                array[i] = value;
            }

            @Override
            public byte[] compress() throws IOException {
                return Snappy.compress(array);
            }
        }

        @Override
        protected NullableArray getNullableArray(int size) {
            return new ByteNullableArray(size);
        }

        final protected void fillCollection(ObjectInput in, RoaringBitmap nullBitmap,
                                            Collection<Byte> collection) throws IOException {
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);

            byte[] array = Snappy.uncompress(bytes);
            int i = 0;
            for (byte value : array) {
                collection.add(nullBitmap.contains(i++) ? null : value);
            }
        }
    }

    final class FieldCollectionBooleanSerializer<T>
            extends FieldSerializer.FieldConstructorSerializer<T, Collection<Boolean>>
            implements CollectionSerializer<T, Collection<Boolean>> {

        protected FieldCollectionBooleanSerializer(Field field, Class<? extends Collection<Boolean>> inputClass) {
            super(field, inputClass);
        }

        @Override
        protected void writeValue(Collection<Boolean> collection, ObjectOutput out) throws IOException, ReflectiveOperationException {
            RoaringBitmap nullBitmap = new RoaringBitmap();
            RoaringBitmap booleanBitmap = new RoaringBitmap();
            int i = 0;
            for (Boolean value : collection) {
                if (value != null) {
                    if (value) {
                        booleanBitmap.add(i);
                    }
                }
                else {
                    nullBitmap.add(i);
                }
                i++;
            }
            out.writeInt(collection.size());
            nullBitmap.writeExternal(out);
            booleanBitmap.writeExternal(out);
        }

        @Override
        final public Collection<Boolean> readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            if (!in.readBoolean()) {
                return null;
            }
            Collection<Boolean> collection = constructor.newInstance();
            boolean[] array = new boolean[in.readInt()];
            RoaringBitmap nullBitmap = new RoaringBitmap();
            RoaringBitmap booleanBitmap = new RoaringBitmap();
            nullBitmap.readExternal(in);
            booleanBitmap.readExternal(in);
            for (int i = 0; i < array.length; i++) {
                if (nullBitmap.contains(i)) {
                    collection.add(null);
                }
                else {
                    collection.add(booleanBitmap.contains(i));
                }
            }
            return collection;
        }
    }

    final class FieldCollectionSerializer<T> extends FieldSerializer.FieldConstructorSerializer<T, Collection<?>>
            implements CollectionSerializer<T, Collection<?>> {

        protected Serializer<Object, ?> componentExternalizer;

        protected FieldCollectionSerializer(Field field, Class<? extends Collection<?>> inputClass) {
            super(field, (Class<? extends Collection<?>>) collectionClass(inputClass));
            componentExternalizer = getGeneric(0);
        }

        @Override
        protected void writeValue(Collection<?> collection, ObjectOutput out) throws IOException, ReflectiveOperationException {
            out.writeInt(collection.size());
            for (Object o : collection) {
                componentExternalizer.writeSerializer(o, out);
            }
        }

        @Override
        public Collection readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            if (!in.readBoolean()) {
                return null;
            }
            Collection collection = constructor.newInstance();
            int size = in.readInt();
            while (size-- > 0) {
                collection.add(componentExternalizer.readObject(in));
            }
            return collection;
        }
    }

    final class FieldMapSerializer<T> extends FieldSerializer.FieldConstructorSerializer<T, Map<?, ?>>
            implements CollectionSerializer<T, Map<?, ?>> {

        private Serializer<Object, ?> keyExternalizer;
        private Serializer<Object, ?> valueExternalizer;

        private FieldMapSerializer(Field field, Class<? extends Map<?, ?>> clazz) {
            super(field, (Class<? extends Map<?, ?>>) collectionClass(clazz));
            keyExternalizer = getGeneric(0);
            valueExternalizer = getGeneric(1);
        }

        @Override
        protected void writeValue(Map<?, ?> map, ObjectOutput out) throws IOException, ReflectiveOperationException {
            out.writeInt(map.size());
            for (Map.Entry entry : map.entrySet()) {
                keyExternalizer.writeSerializer(entry.getKey(), out);
                valueExternalizer.writeSerializer(entry.getValue(), out);
            }
        }

        @Override
        public Map<?, ?> readObject(ObjectInput in) throws IOException, ReflectiveOperationException {
            if (!in.readBoolean()) {
                return null;
            }
            Map map = constructor.newInstance();
            int size = in.readInt();
            while (size-- > 0) {
                map.put(keyExternalizer.readObject(in), valueExternalizer.readObject(in));
            }
            return map;
        }
    }
}
