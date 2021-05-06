package oppucmm.services;

import oppucmm.models.Form;
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
    public int generalStatistic(int x){
        if(x == 0){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(f.id) FROM Form f";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        if(x == 1){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(distinct s.sector) FROM Form s";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        if(x == 2){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(distinct s.academicLevel) FROM Form s WHERE academicLevel = 'Basico'";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        if(x == 3){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(distinct s.academicLevel) FROM Form s WHERE academicLevel = 'Medio'";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        if(x == 4){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(distinct s.academicLevel) FROM Form s WHERE academicLevel = 'Grado'";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        if(x == 5){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(distinct s.academicLevel) FROM Form s WHERE academicLevel = 'Maestria'";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        if(x == 6){
            EntityManager entityManager = getEntityManager();
            String countQ = "SELECT COUNT(distinct s.academicLevel) FROM Form s WHERE academicLevel = 'Doctorado'";
            Query countQuery = entityManager.createQuery(countQ);
            return ((Number) countQuery.getSingleResult()).intValue();
        }
        return x;
    }


}
