/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaexample.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpaexample.controllers.exceptions.NonexistentEntityException;
import jpaexample.entities.Person;
import jpaexample.entities.Relation;

/**
 *
 * @author laq
 */
public class RelationJpaController implements Serializable {

    public RelationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Relation relation) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person friend2 = relation.getFriend2();
            if (friend2 != null) {
                friend2 = em.getReference(friend2.getClass(), friend2.getId());
                relation.setFriend2(friend2);
            }
            Person friend1 = relation.getFriend1();
            if (friend1 != null) {
                friend1 = em.getReference(friend1.getClass(), friend1.getId());
                relation.setFriend1(friend1);
            }
            em.persist(relation);
            if (friend2 != null) {
                friend2.getRelationCollection().add(relation);
                friend2 = em.merge(friend2);
            }
            if (friend1 != null) {
                friend1.getRelationCollection().add(relation);
                friend1 = em.merge(friend1);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Relation relation) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Relation persistentRelation = em.find(Relation.class, relation.getId());
            Person friend2Old = persistentRelation.getFriend2();
            Person friend2New = relation.getFriend2();
            Person friend1Old = persistentRelation.getFriend1();
            Person friend1New = relation.getFriend1();
            if (friend2New != null) {
                friend2New = em.getReference(friend2New.getClass(), friend2New.getId());
                relation.setFriend2(friend2New);
            }
            if (friend1New != null) {
                friend1New = em.getReference(friend1New.getClass(), friend1New.getId());
                relation.setFriend1(friend1New);
            }
            relation = em.merge(relation);
            if (friend2Old != null && !friend2Old.equals(friend2New)) {
                friend2Old.getRelationCollection().remove(relation);
                friend2Old = em.merge(friend2Old);
            }
            if (friend2New != null && !friend2New.equals(friend2Old)) {
                friend2New.getRelationCollection().add(relation);
                friend2New = em.merge(friend2New);
            }
            if (friend1Old != null && !friend1Old.equals(friend1New)) {
                friend1Old.getRelationCollection().remove(relation);
                friend1Old = em.merge(friend1Old);
            }
            if (friend1New != null && !friend1New.equals(friend1Old)) {
                friend1New.getRelationCollection().add(relation);
                friend1New = em.merge(friend1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = relation.getId();
                if (findRelation(id) == null) {
                    throw new NonexistentEntityException("The relation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Relation relation;
            try {
                relation = em.getReference(Relation.class, id);
                relation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The relation with id " + id + " no longer exists.", enfe);
            }
            Person friend2 = relation.getFriend2();
            if (friend2 != null) {
                friend2.getRelationCollection().remove(relation);
                friend2 = em.merge(friend2);
            }
            Person friend1 = relation.getFriend1();
            if (friend1 != null) {
                friend1.getRelationCollection().remove(relation);
                friend1 = em.merge(friend1);
            }
            em.remove(relation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Relation> findRelationEntities() {
        return findRelationEntities(true, -1, -1);
    }

    public List<Relation> findRelationEntities(int maxResults, int firstResult) {
        return findRelationEntities(false, maxResults, firstResult);
    }

    private List<Relation> findRelationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Relation.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Relation findRelation(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Relation.class, id);
        } finally {
            em.close();
        }
    }

    public int getRelationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Relation> rt = cq.from(Relation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
