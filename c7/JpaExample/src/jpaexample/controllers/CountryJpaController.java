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
import jpaexample.entities.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaexample.controllers.exceptions.NonexistentEntityException;
import jpaexample.entities.Country;

/**
 *
 * @author laq
 */
public class CountryJpaController implements Serializable {

    public CountryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Country country) {
        if (country.getPersonCollection() == null) {
            country.setPersonCollection(new ArrayList<Person>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Person> attachedPersonCollection = new ArrayList<Person>();
            for (Person personCollectionPersonToAttach : country.getPersonCollection()) {
                personCollectionPersonToAttach = em.getReference(personCollectionPersonToAttach.getClass(), personCollectionPersonToAttach.getId());
                attachedPersonCollection.add(personCollectionPersonToAttach);
            }
            country.setPersonCollection(attachedPersonCollection);
            em.persist(country);
            for (Person personCollectionPerson : country.getPersonCollection()) {
                Country oldCountryOfPersonCollectionPerson = personCollectionPerson.getCountry();
                personCollectionPerson.setCountry(country);
                personCollectionPerson = em.merge(personCollectionPerson);
                if (oldCountryOfPersonCollectionPerson != null) {
                    oldCountryOfPersonCollectionPerson.getPersonCollection().remove(personCollectionPerson);
                    oldCountryOfPersonCollectionPerson = em.merge(oldCountryOfPersonCollectionPerson);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Country country) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country persistentCountry = em.find(Country.class, country.getId());
            Collection<Person> personCollectionOld = persistentCountry.getPersonCollection();
            Collection<Person> personCollectionNew = country.getPersonCollection();
            Collection<Person> attachedPersonCollectionNew = new ArrayList<Person>();
            for (Person personCollectionNewPersonToAttach : personCollectionNew) {
                personCollectionNewPersonToAttach = em.getReference(personCollectionNewPersonToAttach.getClass(), personCollectionNewPersonToAttach.getId());
                attachedPersonCollectionNew.add(personCollectionNewPersonToAttach);
            }
            personCollectionNew = attachedPersonCollectionNew;
            country.setPersonCollection(personCollectionNew);
            country = em.merge(country);
            for (Person personCollectionOldPerson : personCollectionOld) {
                if (!personCollectionNew.contains(personCollectionOldPerson)) {
                    personCollectionOldPerson.setCountry(null);
                    personCollectionOldPerson = em.merge(personCollectionOldPerson);
                }
            }
            for (Person personCollectionNewPerson : personCollectionNew) {
                if (!personCollectionOld.contains(personCollectionNewPerson)) {
                    Country oldCountryOfPersonCollectionNewPerson = personCollectionNewPerson.getCountry();
                    personCollectionNewPerson.setCountry(country);
                    personCollectionNewPerson = em.merge(personCollectionNewPerson);
                    if (oldCountryOfPersonCollectionNewPerson != null && !oldCountryOfPersonCollectionNewPerson.equals(country)) {
                        oldCountryOfPersonCollectionNewPerson.getPersonCollection().remove(personCollectionNewPerson);
                        oldCountryOfPersonCollectionNewPerson = em.merge(oldCountryOfPersonCollectionNewPerson);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = country.getId();
                if (findCountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
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
            Country country;
            try {
                country = em.getReference(Country.class, id);
                country.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            Collection<Person> personCollection = country.getPersonCollection();
            for (Person personCollectionPerson : personCollection) {
                personCollectionPerson.setCountry(null);
                personCollectionPerson = em.merge(personCollectionPerson);
            }
            em.remove(country);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Country> findCountryEntities() {
        return findCountryEntities(true, -1, -1);
    }

    public List<Country> findCountryEntities(int maxResults, int firstResult) {
        return findCountryEntities(false, maxResults, firstResult);
    }

    private List<Country> findCountryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Country.class));
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

    public Country findCountry(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Country.class, id);
        } finally {
            em.close();
        }
    }
    
    public int getCountryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Country> rt = cq.from(Country.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
