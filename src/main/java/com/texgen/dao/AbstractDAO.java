package com.texgen.dao;

import com.texgen.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractDAO<T> implements GenericDAO<T> {
    private final Class<T> entityClass;

    protected AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
        executeTransaction(session -> session.persist(entity));
    }

    @Override
    public void update(T entity) {
        executeTransaction(session -> session.merge(entity));
    }

    @Override
    public void delete(T entity) {
        executeTransaction(session -> session.remove(entity));
    }

    @Override
    public T findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        }
    }

    public T findByIdByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, username);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass.getName(), entityClass).list();
        }
    }

    private void executeTransaction(DAOOperation operation) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            operation.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            Logger logger = LogManager.getLogger(AbstractDAO.class);
            logger.error("Error executing transaction", e);
        }
    }

    @FunctionalInterface
    private interface DAOOperation {
        void accept(Session session);
    }
}