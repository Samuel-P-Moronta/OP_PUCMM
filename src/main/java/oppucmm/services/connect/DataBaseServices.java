package oppucmm.services.connect;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseServices {
    private String URL = "jdbc:h2:tcp://localhost/~/oppucmm";
    private static DataBaseServices conexionBD;
    private static Server tcp;
    private static Server webServer;

    public DataBaseServices() throws SQLException {
        registroDriver();
    }
    public static DataBaseServices getInstance() throws SQLException {
        if(conexionBD==null){
            conexionBD = new DataBaseServices();
        }
        return conexionBD;
    }

    public static void InciarBD() throws SQLException {
        tcp = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon", "-ifNotExists").start();
        String status = Server.createWebServer( "-webPort", "9091", "-webAllowOthers", "-webDaemon").start().getStatus();
        System.out.println("Status Web: "+status);

    }
    public static void detenerBD() throws SQLException {
        tcp.stop();
    }
    private void registroDriver() {
        try {
            Class.forName("org.h2.Driver");
            System.out.println("DRIVER: [org.h2.Driver] registred");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
            System.out.print("CONNECTION...[SUCCESS]");
        } catch (SQLException te) {
            System.out.println("[FAILED]: "+te.getMessage());

        }
        return con;
    }

}
