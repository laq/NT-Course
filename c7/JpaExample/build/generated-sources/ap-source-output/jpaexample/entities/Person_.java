package jpaexample.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpaexample.entities.Country;
import jpaexample.entities.Relation;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-21T01:05:18")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, Country> country;
    public static volatile SingularAttribute<Person, String> password;
    public static volatile CollectionAttribute<Person, Relation> relationCollection;
    public static volatile SingularAttribute<Person, String> name;
    public static volatile CollectionAttribute<Person, Relation> relationCollection1;
    public static volatile SingularAttribute<Person, Integer> id;

}