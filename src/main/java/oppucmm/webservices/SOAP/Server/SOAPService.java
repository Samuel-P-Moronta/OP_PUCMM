package oppucmm.webservices.SOAP.Server;

import oppucmm.models.Form;
import oppucmm.services.FormService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public class SOAPService {
    public FormService formService = FormService.getInstance();

    @WebMethod
    public List<Form> findAll(){
        return formService.explorarTodo();
    }
    @WebMethod
    public Form create(Form form){
        formService.crear(form);
        return form;
    }

}
