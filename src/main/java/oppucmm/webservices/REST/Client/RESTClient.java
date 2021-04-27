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
    static String url = "http://localhost:7000/restApi/formulario/addForm";
    static String urlMat = "http://localhost:7000/restApi/formAdd/{matricula}";
    static String param = "matricula";
    static String content = "Content-Type";
    static String appJson = "application/json";
    private static JWT j1 = null;


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
        /*Json Image*/
        JsonObject imgJSON = new JsonObject();
        imgJSON.addProperty("fotoBase64", form.getFotoBase64());
        JsonObject st = new JsonObject();
        /*Adding properties*/
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

    public static List<Form> listForm(String usuario) {
        try{
            HttpResponse<String> r1 = Unirest.get("http://localhost:7000/restApi/formulario/listForm" + "{user}").routeParam("user", usuario).asString();
            System.out.println("Status: " + r1.getStatus());
            System.out.println(r1.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
