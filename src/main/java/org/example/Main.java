package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.entities.Product;
import org.example.persistence.CustomPersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String puName = "pu-name";
        Map<String,String> props = new HashMap<>();
        props.put("hibernate.show_sql","true");
        props.put("hibernate.hbm2ddl.auto","none");
        EntityManagerFactory emf = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(puName),props);
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            /*
            String jpql = "SELECT p FROM Product p";
            TypedQuery<Product> q = em.createQuery(jpql, Product.class);
            List<Product> products = q.getResultList();
            for (Product p: products){
                System.out.println(p);
            }
             */
            String jpql = "SELECT p FROM Product p WHERE p.price > 10";
            TypedQuery<Product> q = em.createQuery(jpql, Product.class);
            List<Product> products = q.getResultList();
            for (Product p: products){
                System.out.println(p);
            }


            String jpql2 = "SELECT p FROM Product p WHERE p.name LIKE :name";
            TypedQuery<Product> q2 = em.createQuery(jpql2, Product.class);
            //q2.setParameter("price",5);
            q2.setParameter("name","WATER");
            List<Product> products2 = q2.getResultList();
            for (Product p: products2){
                System.out.println(p);
            }



            String jpql3 = "SELECT SUM(p.price) FROM Product p";
            TypedQuery<BigDecimal> q3 = em.createQuery(jpql3, BigDecimal.class);

            BigDecimal sum = q3.getSingleResult();

            System.out.println(sum);

            String jpql4 = "SELECT COUNT(p) FROM Product p";
            TypedQuery<Long> q4 = em.createQuery(jpql4, Long.class);

            Long count = q4.getSingleResult();

            System.out.println(count + " adet product var.");

            String jpql5 = "SELECT p.name FROM Product p";
            TypedQuery<Object[]> q5 = em.createQuery(jpql5, Object[].class);
            q5.getResultList().forEach(objects -> System.out.println(objects[0] + " " + objects[1]));

            String jpql6 = "SELECT p.name, AVG(p.price) FROM Product p GROUP BY p.name";
            TypedQuery<Object[]> q6 = em.createQuery(jpql6, Object[].class);
            q6.getResultList().forEach(objects -> System.out.println(objects[0] + " ürünün satış ortalaması: " + objects[1]));



            // getResultStream
            // getSingleResult

            em.getTransaction().commit();
        }finally {
            em.close();
        }

    }
}