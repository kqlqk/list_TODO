package me.kqlqk.springBootApp.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class SessionUtil {
    private Session session;
    private Transaction transaction;
    private HibernateUtil hibernateUtil;



    public Session getSession() {
        return session;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Session openSession(){
        session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    public Session openTransactionSession(){
        session = openSession();
        transaction = session.beginTransaction();
        return session;
    }

    public void closeSession(){
        session.close();
    }

    public void closeTransactionSession(){
        transaction.commit();
        closeSession();
    }
}

