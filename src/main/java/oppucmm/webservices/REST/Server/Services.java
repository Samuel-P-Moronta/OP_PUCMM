package oppucmm.webservices.REST.Server;

import oppucmm.controllers.Controller;
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
            aux = new Form(form.getFullName(), form.getSector(), form.getAcademicLevel(),locationAux);
            System.out.println("Form made by "+ form.getUser());
            FormService.getInstance().crear(aux);
            return new Form();
        }
        return null;
    }
    public List<Form> getFormsByUsername() {
        List<Form> forms = null;
        if (forms != null) {
            System.out.println("Estoy por aqui!!");
            forms.forEach((Form form) -> {
                FormService.getInstance().getEntityManager().detach(form);
            });
        }
        return forms;
    }
}
