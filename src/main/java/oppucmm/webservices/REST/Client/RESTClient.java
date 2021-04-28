package oppucmm.webservices.REST.Client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import oppucmm.models.Form;
import oppucmm.models.FormAux;
import oppucmm.models.Photo;
import oppucmm.webservices.REST.Server.JWT;

import java.util.List;

public class RESTClient {
    private static JWT j1 = null;
    static String urlListAllForm = "http://localhost:7000/restApi/formulario";

    /* Sending form to rest server...*/
    public static boolean checkUser(String usuario) {
        HttpResponse<String> repuestaServidor = Unirest.get(String.valueOf(new String[]{"http://localhost:7000/restApi/" + "{autenticar}"})).routeParam("autenticar", usuario).asString();
        Gson gson = new Gson();
        j1 = gson.fromJson(repuestaServidor.getBody(), JWT.class);
        System.out.println("Token recibido... " + j1.getToken());
        if (j1.getToken() != null) {
            return true;
        }
        return false;
    }

    public static boolean addForm(FormAux form) {
        System.out.println("\nSend Parameters to the rest server");
        System.out.println("\n Name: " + form.getFullName() +
                " Sector: " + form.getSector() +
                " Academic Level: " + form.getAcademicLevel());
        JsonObject st = new JsonObject();

        st.addProperty("fullName", form.getFullName());
        st.addProperty("sector", form.getSector());
        st.addProperty("academicLevel", form.getAcademicLevel());
        st.addProperty("latitude", form.getLatitude());
        st.addProperty("longitude", form.getLongitude());


        HttpResponse<JsonNode> repuestaServidor = Unirest.post("http://localhost:7000/restApi/formulario/addForm")
                .header("Content-Type", "application/json").body(st).asJson();
        System.out.println("Status: " + repuestaServidor.getStatus());

        System.out.println("Nuevo Formulario: " + repuestaServidor.getBody());

        return false;
    }

    public static List<Form> listForm() {
        try{
            HttpResponse<String> r1 = Unirest.get(urlListAllForm).asString();
            System.out.println("Status: " + r1.getStatus());
            System.out.println(r1.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
