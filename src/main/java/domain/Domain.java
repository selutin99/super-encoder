package domain;

import service.SerializeService;

import java.io.IOException;

public class Domain {
    public static void main(String[] args) throws IOException {
        MyClass object = new MyClass();
        object.setA(54);
        object.setB(2L);

        byte[] bytes;

        SerializeService service = new SerializeService();
        bytes = service.serialize(object);

        //System.out.println(bytes);
    }
}

class MyClass{
    private int a;
    private long b;

    public MyClass(){

    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }
}
