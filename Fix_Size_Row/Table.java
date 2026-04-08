package Fix_Size_Row;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Table {
    private Schema schema;
    private String filename;

    public Table(Schema schema , String filename){
        this.schema = schema;
        this.filename = filename;
    }

    public void insert( Object[] rowData , int index  ) throws IOException{
        ByteBuffer buf = ByteBuffer.allocate(schema.getRowSize());
        int currentOffSet = 0;

        for(int i = 0 ; i<schema.getFields().size() ; i++ ){
            Field field = schema.getFields().get(i);
            field.write(buf, currentOffSet , rowData[i]);
            currentOffSet+=field.getSize();
        }

        try  (RandomAccessFile file = new RandomAccessFile(filename, "rw")){
            file.seek((long) index * schema.getRowSize() );
            file.write(buf.array());
        }
    }

    public Object[] getRow(int index) throws IOException{
        byte[] bytes = new byte[schema.getRowSize()];
        try ( RandomAccessFile file = new RandomAccessFile(filename, "r") ){
            file.seek((long) index  * schema.getRowSize() );
            file.readFully(bytes);
        }

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        Object[] rowData = new Object[schema.getFields().size()];
        int currentOffset = 0;

        for (int i = 0; i < schema.getFields().size(); i++) {
            Field field = schema.getFields().get(i);
            rowData[i] = field.read(buf, currentOffset);
            currentOffset += field.getSize();
        }
        return rowData;

    }

    public static void main(String[] args) throws IOException {
        // 1. Define the structure (Schema)
        Schema userSchema = new Schema();
        userSchema.addField("id", new IntField());               // 4 bytes
        userSchema.addField("name", new StringField(64));        // 65 bytes
        userSchema.addField("isActive", new BoolenField());      // 1 byte

        // 2. Initialize the Table
        Table userTable = new Table(userSchema, "generic_users.db");

        // 3. Insert data as an array of Objects
        Object[] alice = {101, "Alice", true};
        userTable.insert(alice, 0);

        // 4. Retrieve data
        Object[] result = userTable.getRow(0);
        System.out.println("ID: " + result[0] + ", Name: " + result[1]);
    }
}

