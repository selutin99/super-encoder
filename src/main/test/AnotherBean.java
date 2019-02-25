import service.serializers.FieldSerializer;

import java.time.Instant;

public class AnotherBean {
    private PrimitiveBean primitiveBean;
    private Instant b;

    public PrimitiveBean getPrimitiveBean() {
        return primitiveBean;
    }

    public void setPrimitiveBean(PrimitiveBean primitiveBean) {
        this.primitiveBean = primitiveBean;
    }

    public Instant getB() {
        return b;
    }

    public void setB(Instant b) {
        this.b = b;
    }
}
