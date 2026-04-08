package Fix_Size_Row;
import java.nio.ByteBuffer;

public class IntField implements Field{
    public int getSize() {return 4;};
    public void write( ByteBuffer buf , int offset , Object value ){
        buf.putInt(offset , (Integer) value);
    }
    public Object read(ByteBuffer buf , int offset){
        return buf.getInt(offset);
    }
}