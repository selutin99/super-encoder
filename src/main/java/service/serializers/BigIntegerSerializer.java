package service.serializers;

import contracts.Serializer;
import service.exceptions.SerializeException;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.math.BigInteger;

public interface BigIntegerSerializer<T, V> extends Serializer<T, V> {

    static <T, V> FieldSerializer<T, V> bigInt(Field field, Class<? extends T> inputClass) {
        if (BigInteger.class.isAssignableFrom(inputClass)) {
            return (FieldSerializer<T, V>) new FieldSerializer.FieldParentSerializer(field, BigIntSerializer.INSTANCE);
        }
        return null;
    }

    @Override
    default void readSerializer(T object, ObjectInput in) throws IOException, ReflectiveOperationException {
        throw new SerializeException("Не поддерживается!");
    }

    class BigIntSerializer implements BigIntegerSerializer<BigInteger, BigInteger> {

        static BigIntSerializer INSTANCE = new BigIntSerializer();

        @Override
        public void writeSerializer(BigInteger object, ObjectOutput out) throws IOException {
            if (object != null) {
                out.writeBoolean(true);
                out.writeInt(object.toByteArray().length);
                out.write(object.toByteArray());
            }
            else {
                out.writeBoolean(false);
            }
        }

        @Override
        public BigInteger readObject(ObjectInput in) throws IOException, ClassNotFoundException {
            if(in.readBoolean()){
                int len = in.readInt();
                byte[] val = new byte[len];
                in.read(val);
                return new BigInteger(val);
            }
            else{
                return null;
            }
        }
    }

}
