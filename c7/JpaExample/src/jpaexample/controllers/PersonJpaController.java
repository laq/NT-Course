/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaexample.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpaexample.entities.Country;
import jpaexample.entities.Relation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaexample.controllers.exceptions.NonexistentEntityException;
import jpaexample.entities.Person;

/**
 *
 * @author laq
 */
public class PersonJpaController implements Serializable {

    public PersonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) {
        if (person.getRelationCollection() == null) {
            person.setRelationCollection(new ArrayList<Relation>());
        }
        if (person.getRelationCollection1() == null) {
            person.setRelationCollection1(new ArrayList<Relation>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country country = person.getCountry();
            if (country != null) {
                country = em.getReference(country.getClass(), country.getId());
                person.setCountry(country);
            }
            Collection<Relation> attachedRelationCollection = new ArrayList<Relation>();
            for (Relation relationCollectionRelationToAttach : person.getRelationCollection()) {
                relationCollectionRelationToAttach = em.getReference(relationCollectionRelationToAttach.getClass(), relationCollectionRelationToAttach.getId());
                attachedRelationCollection.add(relationCollectionRelationToAttach);
            }
            person.setRelationCollection(attachedRelationCollection);
            Collection<Relation> attachedRelationCollection1 = new ArrayList<Relation>();
            for (Relation relationCollection1RelationToAttach : person.getRelationCollection1()) {
                relationCollection1RelationToAttach = em.getReference(relationCollection1RelationToAttach.getClass(), relationCollection1RelationToAttach.getId());
                attachedRelationCollection1.add(relationCollection1RelationToAttach);
            }
            person.setRelationCollection1(attachedRelationCollection1);
            em.persist(person);
            if (country != null) {
                country.getPersonCollection().add(person);
                country = em.merge(country);
            }
            for (Relation relationCollectionRelation : person.getRelationCollection()) {
                Person oldFriend2OfRelationCollectionRelation = relationCollectionRelation.getFriend2();
                relationCollectionRelation.setFriend2(person);
                relationCollectionRelation = em.merge(relationCollectionRelation);
                if (oldFriend2OfRelationCollectionRelation != null) {
                    oldFriend2OfRelationCollectionRelation.getRelationCollection().remove(relationCollectionRelation);
                    oldFriend2OfRelationCollectionRelation = em.merge(oldFriend2OfRelationCollectionRelation);
                }
            }
            for (Relation relationCollection1Relation : person.getRelationCollection1()) {
                Person oldFriend1OfRelationCollection1Relation = relationCollection1Relation.getFriend1();
                relationCollection1Relation.setFriend1(person);
                relationCollection1Relation = em.merge(relationCollection1Relation);
                if (oldFriend1OfRelationCollection1Relation != null) {
                    oldFriend1OfRelationCollection1Relation.getRelationCollection1().remove(relationCollection1Relation);
                    oldFriend1OfRelationCollection1Relation = em.merge(oldFriend1OfRelationCollection1Relation);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person persistentPerson = em.find(Person.class, person.getId());
            Country countryOld = persistentPerson.getCountry();
            Country countryNew = person.getCountry();
            Collection<Relation> relationCollectionOld = persistentPerson.getRelationCollection();
            Collection<Relation> relationCollectionNew = person.getRelationCollection();
            Collection<Relation> relationCollection1Old = persistentPerson.getRelationCollection1();
            Collection<Relation> relationCollection1New = person.getRelationCollection1();
            if (countryNew != null) {
                countryNew = em.getReference(countryNew.getClass(), countryNew.getId());
                person.setCountry(countryNew);
            }
            Collection<Relation> attachedRelationCollectionNew = new ArrayList<Relation>();
            for (Relation relationCollectionNewRelationToAttach : relationCollectionNew) {
                relationCollectionNewRelationToAttach = em.getReference(relationCollectionNewRelationToAttach.getClass(), relationCollectionNewRelationToAttach.getId());
                attachedRelationCollectionNew.add(relationCollectionNewRelationToAttach);
            }
            relationCollectionNew = attachedRelationCollectionNew;
            person.setRelationCollection(relationCollectionNew);
            Collection<Relation> attachedRelationCollection1New = new ArrayList<Relation>();
            for (Relation relationCollection1NewRelationToAttach : relationCollection1New) {
                relationCollection1NewRelationToAttach = em.getReference(relationCollection1NewRelationToAttach.getClass(), relationCollection1NewRelationToAttach.getId());
                attachedRelationCollection1New.add(relationCollection1NewRelationToAttach);
            }
            relationCollection1New = attachedRelationCollection1New;
            person.setRelationCollection1(relationCollection1New);
            person = em.merge(person);
            if (countryOld != null && !countryOld.equals(countryNew)) {
                countryOld.getPersonCollection().remove(person);
                countryOld = em.merge(countryOld);
            }
            if (countryNew != null && !countryNew.equals(countryOld)) {
                countryNew.getPersonCollection().add(person);
                countryNew = em.merge(countryNew);
            }
            for (Relation relationCollectionOldRelation : relationCollectionOld) {
                if (!relationCollectionNew.contains(relationCollectionOldRelation)) {
                    relationCollectionOldRelation.setFriend2(null);
                    relationCollectionOldRelation = em.merge(relationCollectionOldRelation);
                }
            }
            for (Relation relationCollectionNewRelation : relationCollectionNew) {
                if (!relationCollectionOld.contains(relationCollectionNewRelation)) {
                    Person oldFriend2OfRelationCollectionNewRelation = relationCollectionNewRelation.getFriend2();
                    relationCollectionNewRelation.setFriend2(person);
                    relationCollectionNewRelation = em.merge(relationCollectionNewRelation);
                    if (oldFriend2OfRelationCollectionNewRelation != null && !oldFriend2OfRelationCollectionNewRelation.equals(person)) {
                        oldFriend2OfRelationCollectionNewRelation.getRelationCollection().remove(relationCollectionNewRelation);
                        oldFriend2OfRelationCollectionNewRelation = em.merge(oldFriend2OfRelationCollectionNewRelation);
                    }
                }
            }
            for (Relation relationCollection1OldRelation : relationCollection1Old) {
                if (!relationCollection1New.contains(relationCollection1OldRelation)) {
                    relationCollection1OldRelation.setFriend1(null);
                    relationCollection1OldRelation = em.merge(relationCollection1OldRelation);
                }
            }
            for (Relation relationCollection1NewRelation : relationCollection1New) {
                if (!relationCollection1Old.contains(relationCollection1NewRelation)) {
                    Person oldFriend1OfRelationCollection1NewRelation = relationCollection1NewRelation.getFriend1();
                    relationCollection1NewRelation.setFriend1(person);
                    relationCollection1NewRelation = em.merge(relationCollection1NewRelation);
                    if (oldFriend1OfRelationCollection1NewRelation != null && !oldFriend1OfRelationCollection1NewRelation.equals(person)) {
                        oldFriend1OfRelationCollection1NewRelation.getRelationCollection1().remove(relationCollection1NewRelation);
                        oldFriend1OfRelationCollection1NewRelation = em.merge(oldFriend1OfRelationCollection1NewRelation);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = person.getId();
                if (findPerson(id) == null) {
                    throw new NonexistentEntityException("The person with id " + id + " no longer exists.");
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
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            Country country = person.getCountry();
            if (country != null) {
                country.getPersonCollection().remove(person);
                country = em.merge(country);
            }
            Collection<Relation> relationCollection = person.getRelationCollection();
            for (Relation relationCollectionRelation : relationCollection) {
                relationCollectionRelation.setFriend2(null);
                relationCollectionRelation = em.merge(relationCollectionRelation);
            }
            Collection<Relation> relationCollection1 = person.getRelationCollection1();
            for (Relation relationCollection1Relation : relationCollection1) {
                relationCollection1Relation.setFriend1(null);
                relationCollection1Relation = em.merge(relationCollection1Relation);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
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

    public Person findPerson(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
