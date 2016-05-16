import com.google.gson.Gson;

/**
 * Created by Aaron on 5/1/16.
 */
public class JSONWriter {
    public static void writeData(SiteData data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        System.out.println(json);
    }
}
