package oppucmm.webservices.REST.Server;

import oppucmm.models.*;
import oppucmm.services.FormService;
import oppucmm.services.UserService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class Services {
    private static Services services = null;

    private Services() {
    }

    public static Services getInstance() {
        if (services == null) {
            services = new Services();
        }
        return services;
    }

    public Form addFormDb(FormAux form) {
        Form aux = null;
        if (form != null) {
            Location locationAux = new Location(form.getLongitude(),form.getLatitude());
            Photo photo = new Photo(form.getFotoBase64());
            aux = new Form(form.getFullName(), form.getSector(), form.getAcademicLevel(),locationAux,photo);
            System.out.println("Se creo el form!!");
            FormService.getInstance().crear(aux);
            return new Form();
        }
        return null;
    }
    public List<Form> getFormsByUsername(String username) {
        User user = UserService.getInstance().buscar(username.trim());
        List<Form> forms = null;
        if (user != null) {
            forms = user.formToGet(user);
            forms.forEach((Form form) -> {
                FormService.getInstance().getEntityManager().detach(form);
            });
        }
        return forms;
    }
}
