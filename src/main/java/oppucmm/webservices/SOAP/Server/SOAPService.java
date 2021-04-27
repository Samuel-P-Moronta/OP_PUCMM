package oppucmm.webservices.SOAP.Server;

import oppucmm.models.Form;
import oppucmm.models.User;
import oppucmm.services.FormService;
import oppucmm.services.UserService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public class SOAPService {
    public FormService formService = FormService.getInstance();
    public UserService userService = UserService.getInstance();

    @WebMethod
    public List<Form> findAll(){
        return formService.explorarTodo();
    }
    @WebMethod
    public Form create(Form form){
        formService.crear(form);
        return form;
    }
    @WebMethod
    public User login(String username, String password){

        return userService.LoginRequest(username,password);
    }

}
