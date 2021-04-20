package oppucmm.services.connect;


import oppucmm.Main;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DataBaseRepository<T> {
    private static EntityManagerFactory entityManagerFactory;
    private Class<T> claseEntidad;

    public DataBaseRepository(Class<T> clase) {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
        }
        this.claseEntidad = clase;
    }
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Permite obtener el valor del campo
     */
    private Object getValorCampo(T entidad){
        Object valorCampo = null;
        if(entidad != null){
            for(Field campo : entidad.getClass().getDeclaredFields()) {
                if (campo.isAnnotationPresent(Id.class)) {
                    try {
                        campo.setAccessible(true);
                        valorCampo = campo.get(entidad);
                        System.out.println("Nombre del campo: "+campo.getName());
                        System.out.println("Tipo del campo: "+campo.getType().getName());
                        System.out.println("Valor del campo: "+valorCampo );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return valorCampo;
    }

    /**
     * CRUD BASICO IMPLEMENTANDO ORM
     * @param entidad
     * @return
     * @throws IllegalArgumentException
     * @throws EntityExistsException
     * @throws PersistenceException
     */
    public boolean crear(T entidad) throws PersistenceException{
       boolean estado = false;
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entidad);
            entityManager.getTransaction().commit();
            estado = true;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return estado;
    }
    public boolean eliminar(Object entidadId) throws PersistenceException {
        boolean estado = false;
        T entidad;
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        try {
            entidad = entityManager.find(this.claseEntidad, entidadId);
            entityManager.remove(entidad);
            entityManager.getTransaction().commit();
            estado = true;
        } finally {
            entityManager.close();
        }

        return estado;
    }
    public boolean editar(T entidad) throws PersistenceException {
        boolean estado = false;
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(entidad);
            entityManager.getTransaction().commit();
            estado = true;
        } finally {
            entityManager.close();
        }

        return estado;
    }

    public T buscar(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            return em.find(claseEntidad, id);
        } catch (Exception ex){
            throw  ex;
        } finally {
            em.close();
        }
    }

    public List<T> explorarTodo() throws PersistenceException {
        EntityManager entityManager = getEntityManager();
        List<T> entida = null;
        try {
            CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(this.claseEntidad);
            criteriaQuery.select(criteriaQuery.from(this.claseEntidad));
            entida = entityManager.createQuery(criteriaQuery).getResultList();
        } finally {
            entityManager.close();
        }
        return entida;
    }

    /**
     * Configurar información de la conexión de Heroku.
     * Tomado de https://gist.github.com/mlecoutre/4088178
     * @return
     */
    private EntityManagerFactory getConfiguracionBaseDatosHeroku(){
        //Leyendo la información de la variable de ambiente de Heroku
        String databaseUrl = System.getenv("DATABASE_URL");
        StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
        //Separando las información del conexión.
        String dbVendor = st.nextToken();
        String userName = st.nextToken();
        String password = st.nextToken();
        String host = st.nextToken();
        String port = st.nextToken();
        String databaseName = st.nextToken();
        //creando la jbdc String
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, databaseName);
        //pasando las propiedades.
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", jdbcUrl );
        properties.put("javax.persistence.jdbc.user", userName );
        properties.put("javax.persistence.jdbc.password", password );
        //
        return Persistence.createEntityManagerFactory("Heroku", properties);
    }


}
