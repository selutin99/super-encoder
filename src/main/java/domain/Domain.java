package domain;

import service.SerializeService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Domain {

    public static void main(String[] args) throws IOException {
        MyClass object = new MyClass();
        object.setA(23);
        object.setB(2L);
        object.setBigInteger(BigInteger.valueOf(23));

        byte[] bytes;

        SerializeService service = new SerializeService(MyClass.class);

        bytes = service.serialize(object);

        System.out.println(Arrays.toString(bytes));

        MyClass obj = (MyClass) service.deserialize(bytes);
        System.out.println(obj.getA() + " " + obj.getBigInteger().intValue());
    }
}
