package com.sergeev.studapp;

import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.jpa.JpaGroupDao;
import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.mongo.MongoUserDao;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Main {
      public static void main1(String[] args) throws PersistentException {
          Group group = new Group();
          Account account = new Account();
          Account account2 = new Account();
          Account account3 = new Account();
          User user = new User();
          User user2 = new User();
          User user3 = new User();

          group.setTitle("AA-2017");

          account.setLogin("kirill_sergeev");
          account.setPassword("111111");

          account2.setLogin("kirill_andreev");
          account2.setPassword("111111");

          account3.setLogin("andrey_andreev");
          account3.setPassword("111111");

          user.setFirstName("Kirill");
          user.setLastName("Sergeev");
          user.setRole(User.Role.ADMIN);
          user.setAccount(account);

          user2.setFirstName("Kirill");
          user2.setLastName("Andreev");
          user2.setRole(User.Role.STUDENT);
          user2.setGroup(group);
          user2.setAccount(account2);

          user3.setFirstName("Andrey");
          user3.setLastName("Andreev");
          user3.setRole(User.Role.STUDENT);
          user3.setGroup(group);
          user3.setAccount(account3);


          group.getStudents().add(user2);
          group.getStudents().add(user3);
          EntityManagerFactory emf = Persistence.createEntityManagerFactory("learn");
          EntityManager em = emf.createEntityManager();
          EntityTransaction tx = em.getTransaction();
          tx.begin();
          em.persist(group);
          em.persist(account);
          em.persist(account2);
          em.persist(account3);
          em.persist(user);
          em.persist(user2);
          em.persist(user3);
          tx.commit();
          em.close();
          emf.close();
    }

    public static void main2(String[] args) {

        Group group = new Group();
        Account account = new Account();
        User user = new User();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("learn");
        EntityManager em = emf.createEntityManager();
        Session session = em.unwrap(Session.class);
        Transaction tx = session.getTransaction();
        tx.begin();
        tx.commit();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);
        List<User> users = session.createQuery(criteria).getResultList();
        System.out.println(users.get(1).getGroup().getStudents().size());
        em.close();
        emf.close();
    }

    public static void main4(String[] args) throws PersistentException {
        Group group = new Group();
        group.setTitle("Hihi");
        JpaGroupDao dao = new JpaGroupDao();

        dao.save(group);
        System.out.println("save: "+group);

        group.setTitle("New");
        dao.update(group);
        System.out.println("update: "+group);

        System.out.println("byid: "+dao.getById(group.getId()));
        System.out.println("all: "+dao.getAll());
        dao.remove(group.getId());
        System.out.println("remove, all: "+dao.getAll());
    }

    public static void main(String[] args) throws PersistentException {
        MongoUserDao dao = new MongoUserDao();

        System.out.println(dao.getByLogin("kirill_sergeev", "111111"));
    }
}