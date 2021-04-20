package oppucmm.services;

import oppucmm.models.Form;
import oppucmm.services.connect.DataBaseRepository;

public class FormService extends DataBaseRepository<Form> {

    private static FormService formService;


    public FormService() {
        super(Form.class);
    }
    public static FormService getInstance(){
        if(formService == null){
            formService = new FormService();
        }
        return formService;
    }

}
