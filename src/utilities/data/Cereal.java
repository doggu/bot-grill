package utilities.data;


import javax.annotation.Nullable;
import java.io.*;

//small serialization class to manage serialization
public class Cereal<T extends Serializable> {
    private static final String CEREAL_SUBDIR = "./cereal";

    private final File file;
    private boolean readable = true;

    /**
     * creates an object which will help deserialize and reserialize objects
     * which are serializable.
     *
     * @param filename the name of the file to store serialized objects in.
     *                 format: "[filename].[extension]"
     * @param path the filepath which will store the serialized object.
     *             format: "/path/"
     * @throws IOException
     */
    @Nullable
    public Cereal(String filename, String path) throws IOException {
        if (new File(CEREAL_SUBDIR+path).mkdirs()) {
            System.out.println("generated filepath: "+CEREAL_SUBDIR+path);
        }

        this.file = new File(CEREAL_SUBDIR+path+filename);

        if (file.createNewFile()) {
            readable = false;
        }

        /*
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("cerealizing...");
                serialize(this.object);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("did not serialize properly. dumping!");
            }
        }));
         */
    }

    public boolean readable() { return readable; }

    public void serialize(T object) throws IOException {
        System.out.println("serializing...");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(object);

        oos.flush();

        oos.close();
        fos.close();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public T deserialize() throws IOException {
        System.out.println("deserializing...");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream oos = new ObjectInputStream(fis);

        T object;
        try {
            object = (T) oos.readObject();
                 //idk but this covers casting and io exceptions
        } catch (Exception e) {
            System.out.println("i had some issues");
            return null;
        }

        oos.close();
        fis.close();

        return object;
    }
}
