package com.freshspire.api.dao;

import com.freshspire.api.model.Product;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductDAOImpl implements ProductDAO {


    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void addProduct(Product product) {
        Session session = getCurrentSession();
        session.persist(product);
        logger.info("Product saved : " + product);
    }

    @Override
    public void deleteProduct(Product product) {
        Session session = getCurrentSession();
        session.delete(product);
        logger.info("Product deleted: " + product);
    }

    @Override
    public void updateProduct(Product product) {
        Session session = getCurrentSession();
        session.update(product);
        logger.info("Product updated : " + product);
    }

    @Override
    public Product getProductById(int productId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Product P where P.productId = :productId");
        query.setParameter("productId", productId);
        Product product = (Product) query.uniqueResult();

        return product;
    }

    @Override
    public List<Product> getProductsByChain(int chainId) {
        Session session = getCurrentSession();
        Query query = session.createQuery("From Product P where P.chainId = :chainId");
        query.setParameter("chainId", chainId);
        return query.list();
    }


    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
