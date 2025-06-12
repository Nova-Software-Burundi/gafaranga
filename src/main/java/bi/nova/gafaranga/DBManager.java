package bi.nova.gafaranga;

import org.iq80.leveldb.*;
import java.io.File;
import static org.fusesource.leveldbjni.JniDBFactory.*;

public class DBManager {
    private static DB db;

    public static void init() {
        try {
            Options options = new Options();
            options.createIfMissing(true);
            db = factory.open(new File("chainData"), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void put(String key, String value) {
        db.put(bytes(key), bytes(value));
    }

    public static String get(String key) {
        byte[] value = db.get(bytes(key));
        return value != null ? asString(value) : null;
    }

    public static void close() {
        try {
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
