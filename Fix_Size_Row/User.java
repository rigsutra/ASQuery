import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class User {
    public static final int ROW_SIZE = 138;

    private int id;
    private String name;
    private int age;
    private String email;

    public User(int id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // Getters for Main method to work
    public int getId() { return id; }
    public String getName() { return name; }

    public ByteBuffer pack() {
        ByteBuffer buf = ByteBuffer.allocate(ROW_SIZE);

        // ID
        buf.putInt(0, this.id);

        // Name
        byte[] nameBytes = this.name.getBytes(StandardCharsets.UTF_8);
        buf.put(4, (byte) Math.min(nameBytes.length, 64));
        buf.position(5);
        buf.put(nameBytes, 0, Math.min(nameBytes.length, 64));

        // Age
        buf.putInt(69, this.age);

        // Email
        byte[] emailBytes = this.email.getBytes(StandardCharsets.UTF_8);
        buf.put(73, (byte) Math.min(emailBytes.length, 64));
        buf.position(74);
        buf.put(emailBytes, 0, Math.min(emailBytes.length, 64));

        return buf;
    }

    public static User unpack(ByteBuffer buf) {
        int id = buf.getInt(0);

        int nameLen = buf.get(4) & 0xFF;
        byte[] nameBytes = new byte[nameLen];
        buf.position(5);
        buf.get(nameBytes);
        String name = new String(nameBytes, StandardCharsets.UTF_8);

        int age = buf.getInt(69);

        int emailLen = buf.get(73) & 0xFF;
        byte[] emailBytes = new byte[emailLen];
        buf.position(74);
        buf.get(emailBytes);
        String email = new String(emailBytes, StandardCharsets.UTF_8);

        return new User(id, name, age, email);
    }

    // Static helper methods for the database operations
    public static void saveUser(User user, int index) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile("users.db", "rw")) {
            long position = (long) index * ROW_SIZE;
            file.seek(position);
            file.write(user.pack().array());
        }
    }

    public static User readUser(int index) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile("users.db", "r")) {
            long position = (long) index * ROW_SIZE;
            file.seek(position);
            byte[] bytes = new byte[ROW_SIZE];
            file.readFully(bytes);
            return User.unpack(ByteBuffer.wrap(bytes));
        }
    }

    public static void main(String[] args) throws IOException {
//        User user1 = new User(101, "Alice", 25, "alice@example.com");
//        User user2 = new User(102, "Bob", 30, "bob@test.com");
//
//        System.out.println("Saving users to users.db...");
//        saveUser(user1, 2);
//        saveUser(user2, 3);

        System.out.println("Reading Bob from slot 1...");
        User fetched = readUser(2);
        User fetched01 = readUser(3);

        System.out.println("Success! Fetched User Data:");
        System.out.println("ID: " + fetched.getId());
        System.out.println("Name: " + fetched.getName());

        System.out.println("Success! Fetched User Data:");
        System.out.println("ID: " + fetched01.getId());
        System.out.println("Name: " + fetched01.getName());
    }
}