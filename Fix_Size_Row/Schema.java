package Fix_Size_Row;
import java.util.ArrayList;
import java.util.List;

public class Schema {
    private List<Field> fields;
    private List<String> columnNames;
    private int totalRowSize;

    public Schema(){
        this.fields = new ArrayList<>();
        this.columnNames = new ArrayList<>();
        this.totalRowSize = 0;
    }

    public void addField(String name , Field field){
        columnNames.add(name);
        fields.add(field);
        totalRowSize+=field.getSize();
    }

    public int getRowSize(){return totalRowSize; }
    public List<Field> getFields() {return fields; }

    public int getOffsetOfField(int fieldIndex) {
        int offset = 0;
        for (int i = 0; i < fieldIndex; i++) {
            offset += fields.get(i).getSize();
        }
        return offset;
    }


}