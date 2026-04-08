package Fix_Size_Row;
import java.nio.ByteBuffer;

public interface Field {
    int getSize();
    void write(ByteBuffer buf , int offset , Object value);
    Object read( ByteBuffer buf, int offfset );
}