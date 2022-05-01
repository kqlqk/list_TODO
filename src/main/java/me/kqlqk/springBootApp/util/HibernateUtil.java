package me.kqlqk.springBootApp.util;

import org.apache.catalina.core.StandardService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    public static SessionFactory getSessionFactory(){
        Configuration config = new Configuration();
        config.configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        return sessionFactory;
    }
}
