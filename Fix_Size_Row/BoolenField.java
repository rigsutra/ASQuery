package Fix_Size_Row;
import java.nio.ByteBuffer;

public class BoolenField implements Field {
    public int getSize() { return 1; }

    public void write(ByteBuffer buf , int offset , Object value){
        // we have the size and we only need one data to put 1 or 0
        boolean val = (Boolean) value;
        buf.put(offset , (byte) (val ? 1 : 0));
    }

    public Object read(ByteBuffer buf , int offset){
        byte b = buf.get(offset);
        return b!=0;
    }
}