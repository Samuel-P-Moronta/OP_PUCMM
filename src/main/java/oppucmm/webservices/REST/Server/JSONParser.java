package oppucmm.webservices.REST.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONParser {
    private static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static boolean isJson(String Json) {
        Gson gson = new Gson();
        try {
            gson.fromJson(Json, Object.class);
            Object jsonObjType = gson.fromJson(Json, Object.class).getClass();
            if(jsonObjType.equals(String.class)){
                return false;
            }
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

}
