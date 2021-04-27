package oppucmm.controllers;

import io.javalin.Javalin;
import oppucmm.models.Form;
import oppucmm.models.User;
import oppucmm.models.RoleApp;
import oppucmm.services.UserService;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserController {
    private static String mpCryptoPassword = "BornToFight";

    static String URI = "";
    private Javalin app;
    private Map<String, Object> model = new HashMap<>();

    public UserController(Javalin app) {
        //super(app);
        this.app = app;
    }

    public void aplicarRutas() {
        app.get("/", ctx -> ctx.redirect("/formularios"));
        app.routes(() -> {
            path("/", () -> {
                before("/", ctx -> {
                    if (ctx.sessionAttribute("user") == null) {
                        ctx.redirect("/login");
                    }
                });
                get("/userRegister", ctx -> {
                    // esta ruta deberia ser la que esta afuera con el login
                    Map<String, Object> modelo = new HashMap<>();
                    List<User> auxUser = UserService.getInstance().explorarTodo();
                    modelo.put("listUser",auxUser);
                    ctx.render("public/register.html", modelo);
                });
            });
        });
        app.post("/userRegister", ctx -> {
            Map<String, Object> modelo = new HashMap<>();

            String fullName = ctx.formParam("fullName");
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            String rol = ctx.formParam("role");
            System.out.println("el valor es> " + ctx.formParam("role"));
            User aux = new User(fullName, username, password);
            if (rol != null) {
                if (rol.matches("Administrador"))
                    aux.setRolesList(Set.of(RoleApp.ROLE_ADMIN,RoleApp.ROLE_EMPLEADO));

                if (rol.matches("Empleado"))
                    aux.setRolesList(Set.of(RoleApp.ROLE_EMPLEADO));

                if (Controller.getInstance().getUserByUsername(username) == null) {
                    Controller.getInstance().addUser(aux);
                    model.put("Success","Usuario creado de forma exitosa!");
                    ctx.sessionAttribute("user", username);
                    ctx.redirect("/formularios");
                    System.out.println("CREADO");
                } else {
                    modelo.put("Error", "El nombre de usuario ya existe. Intentelo de nuevo! ");
                    ctx.render("public/register.html", modelo);
                    System.out.println("NO SE PUDO CREAR");
                }
            } else {
                System.out.println("Se debe asignar un rol");
            }
        });
        app.post("/login", ctx -> {
            String rememberMe = "";
            Map<String, Object> modelo = new HashMap<>();
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            //Authenticator
            User aux = UserService.getInstance().buscar(username);
            if (aux != null) {
                if(aux.getPassword().equals(password)){
                    rememberMe = ctx.formParam("rememberMe");
                    if (rememberMe != null) {
                        if (rememberMe.equalsIgnoreCase("ON")) {
                            System.out.println("Creando cookie...\n");
                            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                            encryptor.setPassword(mpCryptoPassword);
                            encryptor.encrypt(aux.getPassword());
                            ctx.cookie("username", aux.getUsername(), 604800);
                            ctx.cookie("password", encryptor.encrypt(aux.getPassword()), 604800);
                        } else {
                            System.out.println("Cookie no pudo ser creada...\n");
                        }
                    }
                    System.out.println("username "+ aux.getUsername()+" entered to system [OP-PUCMM]");
                    ctx.sessionAttribute("user", username);
                    ctx.redirect("/formularios");
                }else{
                    System.out.println("User: "+aux.getUsername()+" entered an incorrect password");
                    modelo.put("Error","Contraseña incorrecta!");
                }
            } else {
                System.out.println("User is incorrect!");
                modelo.put("Error", "Credenciales incorrectas! ");
            }
            ctx.render("public/page-login.html", modelo);

        });
        app.post("/formularios", ctx -> {
            // esta ruta deberia ser la que esta afuera con el login
          //  ctx.sessionAttribute("username", null);
            User u1 = Controller.getInstance().getUserByUsername(ctx.sessionAttribute("user"));
            List<Form> f1 = getFormByUser(Controller.getInstance().listForm(), u1);

            model.put("user", u1.getUsername());
            model.put("forms", f1);
            Map<String, Object> modelo = new HashMap<>();
        //    modelo.put("user",ctx.sessionAttribute("user"));
            ctx.render("public/form.html", modelo);
        });
        app.get("/login", ctx -> {
            ctx.render("public/page-login.html");
        });
        app.get("/logout", ctx -> {
            System.out.println("\nEliminando cokkie...");
            ctx.clearCookieStore();
            ctx.sessionAttribute("user", null);
            ctx.redirect("/login");
        });
    }
    private List<Form> getFormByUser(List<Form> formService, User user) {
        List<Form> list = new ArrayList<Form>();
        System.out.println("[...] Searching forms");

        for (Form f : formService) {
            if (f.getUser().equals(user)) {
                System.out.println("[...] Form created by: " + f.getUser());
                list.add(f);
            }
        }

        return list;
    }
}
