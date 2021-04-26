package oppucmm.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import oppucmm.models.*;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

public class WebSocketController extends ControladorBase {
    //Creando el repositorio de las sesiones recibidas.
    public static List<Session> usuariosConectados = new ArrayList<>();

    public WebSocketController(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        webSockert();

    }

    public void webSockert(){
        /**
         * Definición del WebSockert en Javalin en contexto.
         */
        app.ws("/conectarServidor", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Conexion Iniciada - "+ctx.getSessionId());
                usuariosConectados.add(ctx.session);

            });
            ws.onMessage(ctx -> {
                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println("Recibiendo Mesaje por WS...");
                List<FormAux> f1 = objectMapper.readValue(ctx.message(), new TypeReference<List<FormAux>>() {});
               // Controller.getInstance().addForm((Form) f1);
                System.out.println("Cantidad de Formularios Recibido: "+addFormDb(f1));
                System.out.println("Mensaje Recibido de "+ctx.getSessionId()+" ====== ");
                System.out.println("Mensaje: "+ctx.message());
                System.out.println("================================");

            });
            ws.onClose(ctx -> {
                System.out.println("La conexión se ha  cerrada - "+ctx.getSessionId());
                usuariosConectados.remove(ctx.session);
            });
            ws.onError(ctx -> {
                System.out.println("Ocurrió un error en el WS");
            });
            ws.onBinaryMessage(ctx -> {
                System.out.println("Mensaje Recibido Binario "+ctx.getSessionId()+" ====== ");
            });

        });

    }

    private int addFormDb(List<FormAux> f1) {
        Form aux = null;
        User auxUsuario = null;
        if(f1.size()>0){
            auxUsuario = Controller.getInstance().getUserByUsername(f1.get(0).getUser());
        }
        for (FormAux f: f1) {
            Location locationAux = new Location(f.getLongitude(),f.getLatitude());
            Photo photo = new Photo(f.getFotoBase64());

            aux = new Form(photo, f.getFullName(),f.getSector(),f.getAcademicLevel(),auxUsuario,locationAux);
            Controller.getInstance().addForm(aux);
        }
        return  f1.size();

    }
}
