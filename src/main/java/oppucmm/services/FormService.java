package oppucmm.services;

import oppucmm.models.Form;
import oppucmm.models.FormAux;
import oppucmm.models.User;
import oppucmm.services.connect.DataBaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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

    public List<Form> findFormsByHash(User user) {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT f FROM Form f WHERE f.user.username = :username");
        query.setParameter("username",user.getUsername());
        return query.getResultList();
    }
}
