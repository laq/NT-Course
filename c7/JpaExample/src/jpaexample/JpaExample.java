/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaexample;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpaexample.controllers.CountryJpaController;
import jpaexample.controllers.PersonJpaController;
import jpaexample.controllers.RelationJpaController;
import jpaexample.entities.Country;
import jpaexample.entities.Person;
import jpaexample.entities.Relation;

/**
 *
 * @author laq
 */
public class JpaExample {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JpaExamplePU");
    static CountryJpaController countryController = new CountryJpaController(emf);
    static PersonJpaController personController = new PersonJpaController(emf);
    static RelationJpaController relationController = new RelationJpaController(emf);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createCountriesIfEmpty();
        createPersonsIfEmpty();
        createRelationsIfEmpty();
        System.out.println("Person id 1:");
        Person p =personController.findPerson(1);
        System.out.println(p);
        
        System.out.println("All persons");
        personController.findPersonEntities().forEach(person -> System.out.println(person));
        
        System.out.println("All persons with their country and relations");
        List<Person> persons = personController.findPersonEntities();
        for(Person person : persons){
            System.out.print(person.getName());
            System.out.print(" "+person.getCountry());
            System.out.println(" Relations:");
            for(Relation r: person.getRelationCollection()){
                System.out.print("\t"+r.getFriend1());
            }
            for(Relation r: person.getRelationCollection1()){
                System.out.print("\t"+r.getFriend2());
            }
            
            System.out.println("");
        }
    }

    private static void createCountriesIfEmpty() {
        if(countryController.getCountryCount () == 0){
            Country c1 = new Country();
            c1.setName("Pais Gotico");
            Country c2 = new Country();
            c2.setName("Kriptolandia");
            Country c3 = new Country();
            c3.setName("Grecia");
            countryController.create(c1);
            countryController.create(c2);
            countryController.create(c3);            
        }
    }

    private static void createPersonsIfEmpty() {
        if(personController.getPersonCount() == 0){
            List<Country> countries = countryController.findCountryEntities();
            
            Person p1 = new Person();
            p1.setName("Batman");            
            p1.setPassword("nanana");
            p1.setCountry(countries.get(1));
            Person p2 = new Person();
            p2.setName("Mujer Maravilla");
            p2.setPassword("hera");
            p2.setCountry(countries.get(2));
            Person p3 = new Person();
            p3.setName("Aquaman");
            p3.setPassword("Atlantida");
            Person p4 = new Person();
            p4.setName("Superman");
            p4.setPassword("Jimmy");
            p4.setCountry(countries.get(0));
            personController.create(p1);
            personController.create(p2);
            personController.create(p3);
            personController.create(p4);
        }
    }

    private static void createRelationsIfEmpty() {
        if(relationController.getRelationCount() == 0){
            
            Relation r = new Relation();
            r.setFriend1(personController.findPerson(1));
            r.setFriend2(personController.findPerson(2));
            relationController.create(r);
            
            r = new Relation();
            r.setFriend1(personController.findPerson(1));
            r.setFriend2(personController.findPerson(3));
            relationController.create(r);
            
        }
    }


    
}
