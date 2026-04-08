package Fix_Size_Row;

import java.nio.ByteBuffer;

public class DateField implements Field {
    private static final int SIZE = 8;

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public void write(ByteBuffer buf, int offset, Object value) {
        buf.putLong(offset, ((java.time.LocalDate) value).toEpochDay());
    }

    @Override
    public Object read(ByteBuffer buf, int offset) {
        long epochDay = buf.getLong(offset);
        return java.time.LocalDate.ofEpochDay(epochDay);
    }
}
