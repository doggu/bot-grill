package utilities.data.cerealtwo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import feh.characters.skills.SkillDatabase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GsonFactoryTest {
//    private static final Gson gson = new Gson();

//    static {
//
//    }

    public static void main(String[] args)
            throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        System.out.println(gson.toJson(SkillDatabase.DATABASE.getRandom()));

        File test = new File("./src/utilities/data/cerealtwo/test.json");

        FileReader reader = new FileReader(test);

        JsonReader jsonReader = gson.newJsonReader(reader);

        jsonReader.beginArray();

        while (jsonReader.hasNext()) {
            //open braces
            jsonReader.beginObject();

            //hero name
            System.out.println(jsonReader.nextName());
            System.out.println(jsonReader.nextString());

            //wiki name
            System.out.println(jsonReader.nextName());
            System.out.println(jsonReader.nextString());

            //5* lv1 stats
            for (int i=0; i<5; i++) {
                System.out.println(jsonReader.nextName());
                System.out.println(jsonReader.nextInt());
            }

            //3* growth rates
            for (int i=0; i<5; i++) {
                System.out.println(jsonReader.nextName());
                System.out.println(jsonReader.nextInt());
            }

            //close braces
            jsonReader.endObject();
        }

        jsonReader.endArray();
        jsonReader.close();

//        gson.fromJson(jsonReader, Hero.class);

         /**/
    }
}
