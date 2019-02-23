package domain;

import service.SerializeService;

import java.io.IOException;

public class Domain {
    public static void main(String[] args) throws IOException {
        MyClass object = new MyClass();
        object.setA("test");
        object.setB(2L);

        byte[] bytes;

        SerializeService service = new SerializeService();
        bytes = service.serialize(object);


    }
}

class MyClass{
    private String a;
    private long b;

    public MyClass(){

    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }
}
