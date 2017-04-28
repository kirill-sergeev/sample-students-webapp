package com.sergeev.studapp.orm;

import com.sergeev.studapp.dao.GroupDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OrmGroupDao extends OrmGenericDao<Group> implements GroupDao {

    private Session currentSession;
    private Transaction currentTransaction;

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("learn");
//        Configuration configuration = new Configuration().configure();
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
//                .applySettings(configuration.getProperties());
//        return configuration.buildSessionFactory(builder.build());
        return emf.unwrap(SessionFactory.class);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    @Override
    public Group save(Group object) throws PersistentException {
        Integer id = (Integer) getCurrentSession().save(object);
        object.setId(id);
        return object;
    }

    @Override
    public Group getById(Integer id) throws PersistentException {
        return getCurrentSession().get((Group.class), id);
    }

    @Override
    public Group update(Group object) throws PersistentException {
        getCurrentSession().update(object);
        return object;
    }

    @Override
    public void delete(Integer id) throws PersistentException {
        getCurrentSession().delete(new Group().setId(id));

//        Object persistentInstance =  getCurrentSession().load(this.getClass(), id);
//        if (persistentInstance != null) {
//            getCurrentSession().delete(persistentInstance);
//        }
    }

    @Override
    public List<Group> getAll() throws PersistentException {
        List<Group> objects = (List<Group>) getCurrentSession().createQuery("from Group").list();
        return objects;
    }
}
