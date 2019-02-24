package domain;

import service.SerializeService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Domain {
    public static void main(String[] args) throws IOException {
        MyClass object = new MyClass();
        object.setA(23);
        object.setB(2L);

        byte[] bytes = null;

        //SerializeService service = new SerializeService();
        //bytes = service.serialize(object);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            SerializeService.serializeRaw(object, output);
            bytes = output.toByteArray();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            MyClass obj = SerializeService.deserializeRaw(input, MyClass.class);
            System.out.println(obj.getA());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
