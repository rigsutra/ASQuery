package Fix_Size_Row;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringField implements Field {
    private int maxChars;

    public StringField(int maxChars) {this.maxChars = maxChars;};

    public int getSize() { return maxChars +1; }; // one extra is for the offset

    public void write(ByteBuffer buf , int offset , Object value  ){
        // check the size the of string and this is done by convering them into the byn
        byte[] bytes = ((String) value).getBytes(StandardCharsets.UTF_8);
        // put this value in the first place as the offset
        buf.put(offset , (byte) Math.min(bytes.length , maxChars) );
        // now we shit one position
        buf.position(offset + 1);
        buf.put(bytes , 0 , Math.min(bytes.length , maxChars));
    }

    public  Object read(ByteBuffer buf , int offset){
        //we will read the offset but we have to know the position of the offset first
        int lengthToRead = buf.get(offset) & 0xFF;
        // create a array to store the bytes of the size lenghttoread
        byte[] bytes = new byte[lengthToRead];
        // now we ger the value and put it in the bytes array
         buf.position(offset +1);
        buf.get( bytes , 0 , lengthToRead );
        return  new String(bytes , StandardCharsets.UTF_8);
    }
}