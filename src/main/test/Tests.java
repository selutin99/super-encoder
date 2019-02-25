import org.junit.Test;
import service.SerializeService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Tests {

    private static byte[] checkSerialize(Class<?> inputClass, Object object){
        byte[] bytes;
        SerializeService service = new SerializeService(inputClass);
        bytes = service.serialize(object);

        return bytes;
    }

    private static Object checkDeserialize(Class<?> inputClass, byte[] data){
        SerializeService service = new SerializeService(inputClass);
        Object obj = service.deserialize(data);
        return obj;
    }

    @Test
    public void primitive() throws Exception {
        PrimitiveBean prim = new PrimitiveBean();
        prim.setA((byte)45);

        byte[] val = checkSerialize(PrimitiveBean.class, prim);
        checkDeserialize(PrimitiveBean.class, val);
    }

    @Test
    public void lang() throws Exception {
        LangBean lang = new LangBean();
        lang.setS1("test");

        byte[] val = checkSerialize(LangBean.class, lang);
        LangBean langBean = (LangBean) checkDeserialize(LangBean.class, val);
        langBean.getS1();
    }

    /**
     * Здесь тест завалится и выпадет SerializeException
     * из-за циклической ссылки
     */
    @Test
    public void cyclicReference() throws Exception {
        A a = new A();
        checkSerialize(A.class, a);
    }

    /**
     * Здесь тест завалится и выпадет
     * из-за отсутсвия конструктора по умолчанию
     */
    @Test
    public void notEmptyContructor() throws Exception {
        Contr cr = new Contr(3, new ArrayList<>());
        checkSerialize(Contr.class, cr);
    }

    @Test
    public void instantAndOtherBean() throws Exception {
        PrimitiveBean primitiveBean = new PrimitiveBean();
        AnotherBean another = new AnotherBean();
        another.setPrimitiveBean(primitiveBean);

        LocalDate localDate = LocalDate.parse("2016-04-17");
        LocalDateTime localDateTime = localDate.atStartOfDay();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

        another.setB(instant);

        checkSerialize(AnotherBean.class, another);
    }
}
