package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.time.Instant;

public interface TimeSerializer<T, V> extends Serializer<T, V> {

    static <T, V> Serializer<T, V> time(Class<? extends T> inputClass) {
        if (Instant.class.isAssignableFrom(inputClass)){
            return (Serializer<T, V>) InstantSerializer.INSTANCE;
        }
        return null;
    }

    static <T, V> Serializer<T, V> time(Field field, Class<? extends T> inputClass) {
        if (Instant.class.isAssignableFrom(inputClass)) {
            return (Serializer<T, V>) new FieldSerializer.FieldParentSerializer(field, InstantSerializer.INSTANCE);
        }
        return null;
    }

    @Override
    default void readSerializer(T object, ObjectInput in) throws IOException, ReflectiveOperationException {
        throw new SerializeException("Не поддерживается");
    }

    final class InstantSerializer implements TimeSerializer<Instant, Instant> {

        static InstantSerializer INSTANCE = new InstantSerializer();

        @Override
        public void writeSerializer(Instant object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeLong(object.getEpochSecond());
                out.writeInt(object.getNano());
            } else {
                out.writeBoolean(false);
            }
        }

        @Override
        public Instant readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            return in.readBoolean() ? Instant.ofEpochSecond(in.readLong(), in.readInt()) : null;
        }
    }
}
