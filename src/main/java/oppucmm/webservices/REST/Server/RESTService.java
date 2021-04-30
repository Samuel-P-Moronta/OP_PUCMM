package oppucmm.webservices.REST.Server;

import oppucmm.models.Form;
import oppucmm.models.User;
import oppucmm.services.FormService;
import oppucmm.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class RESTService {

    private FormService formService = FormService.getInstance();
    private UserService userService = UserService.getInstance();
    public RESTService() {
    }
    public List<Form> findFormsByUser(User user){
        List<Form> forms = new ArrayList<>();
        List<String> jsons = new ArrayList<>();
        forms = formService.findFormsByHash(user);
        return forms;

    }

    public Boolean addForm(Form form){
       return formService.crear(form);
    }

    public User logIn(String username, String password){
        return userService.LoginRequest(username,password);
    }

}
